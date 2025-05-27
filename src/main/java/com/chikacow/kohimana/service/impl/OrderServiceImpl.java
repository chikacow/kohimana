package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.request.OrderRequestDTO;
import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.*;
import com.chikacow.kohimana.model.redis.RedisOrder;
import com.chikacow.kohimana.repository.OrderItemRepository;
import com.chikacow.kohimana.repository.OrderRepository;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.repository.SeatRepository;
import com.chikacow.kohimana.service.*;
import com.chikacow.kohimana.util.enums.OrderStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final SeatService searService;
    private final RedisOrderService redisOrderService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(username);
        User activeUser = userService.getByUsername(username);

        Seat seat = searService.getSeatById(request.getSeatID());

        List<OrderItemRequestDTO> orderItemRequestDTOList = request.getItems();

        Set<OrderItem> orderItems = orderItemService.createOrderItemsFromDTO(orderItemRequestDTOList);

        Order order = Order.builder()
                .seat(seat)
                .user(activeUser)
                .status(OrderStatus.PENDING)
                .build();

        for (OrderItem items : orderItems) {
            items.setOrder(order);
        }
        order.setItems(orderItems);
        order.setTotalAmount(order.calculateTotal());


        Order savedOrder = orderRepository.save(order);

        ///redis
        String ord_id_redis = redisOrderService.save(savedOrder.getId().toString());
        log.info("Order save at redis: {}", ord_id_redis);


        OrderResponseDTO res = OrderResponseDTO.builder()
                .id(savedOrder.getId())
                .seatID(savedOrder.getSeat().getId())
                .userID(savedOrder.getUser().getId())
                .items(orderItemService.convertToDTOs(savedOrder.getItems()))
                .totalAmount(savedOrder.getTotalAmount())
                .createdAt(savedOrder.getCreatedAt())
                .status(OrderStatus.PENDING)
                .build();

        return res;
    }

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);

        OrderResponseDTO res = OrderResponseDTO.builder()
                .id(savedOrder.getId())
                .status(savedOrder.getStatus())
                .userID(savedOrder.getUser().getId())
                .build();
        return res;
    }

    public List<OrderResponseDTO> getOrdersBySeat(Long seatId) {
        List<Order> list = orderRepository.findBySeatId(seatId);

        List<OrderResponseDTO> res = new ArrayList<>();

        for (Order order : list) {
            OrderResponseDTO r = OrderResponseDTO.builder()
                    .id(order.getId())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .userID(order.getUser().getId())
                    .build();
            res.add(r);
        }

        return res;
    }

    public List<OrderResponseDTO> getOrdersByStatus(List<OrderStatus> status) {

        List<Order> list = orderRepository.findByStatusIn(status);

        List<OrderResponseDTO> res = new ArrayList<>();
        for (Order order : list) {
            OrderResponseDTO r = OrderResponseDTO.builder()
                    .id(order.getId())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .userID(order.getUser().getId())
                    .build();

            res.add(r);
        }

        return res;

    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found"));
    }


    /**
     * Not so optimizing for a humble number of records ?
     * @return
     */
    @Override
    public List<OrderResponseDTO> getLatestOrders() {
        List<RedisOrder> redisOrders = redisOrderService.getAll();
       // List<OrderResponseDTO> orderReal = getOrdersByStatus(List.of(OrderStatus.PENDING));
        //PriorityQueue<RedisOrder> queue = new PriorityQueue<>();

        ///query builder
//        StringBuilder sb = new StringBuilder();
//        for (RedisOrder redisOrder : redisOrders) {
//            sb.append(redisOrder.getId()).append(",");
//        }
//        sb.replace(sb.length() - 1, sb.length(), "");

        List<String> id_list = new ArrayList<>();
        for (RedisOrder redisOrder : redisOrders) {
            id_list.add(redisOrder.getId());
        }

        List<Order> orderList = entityManager
                .createQuery("SELECT o FROM Order o WHERE o.id IN :ids", Order.class)
                .setParameter("ids", id_list)
                .getResultList();

        orderList.sort(Comparator.comparing(Order::getCreatedAt));

        List<OrderResponseDTO> res = new ArrayList<>();
        for (Order order : orderList) {
            res.add(OrderResponseDTO.builder()
                    .id(order.getId())
                    .status(order.getStatus())
                    .createdAt(order.getCreatedAt())
                    .userID(order.getUser().getId())
                    .build());
        }


        return res;
    }

    private boolean checkAuthorization(String username) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            return true;
        }
        return false;

    }
}
