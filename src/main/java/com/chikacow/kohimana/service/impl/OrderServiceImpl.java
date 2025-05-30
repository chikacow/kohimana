package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.request.OrderRequestDTO;
import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.mapper.OrderMapper;
import com.chikacow.kohimana.model.*;
import com.chikacow.kohimana.model.redis.RedisOrder;
import com.chikacow.kohimana.repository.OrderItemRepository;
import com.chikacow.kohimana.repository.OrderRepository;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.repository.SeatRepository;
import com.chikacow.kohimana.service.*;
import com.chikacow.kohimana.util.enums.OrderStatus;
import com.chikacow.kohimana.util.helper.Converter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Or;
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

    @Override
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        Set<OrderItem> orderItems = orderItemService.createOrderItemsFromDTO(request.getItems());

        Order order = Order.builder()
                .seat(searService.getSeatById(request.getSeatID()))
                .user(getCustomer())
                .status(OrderStatus.PENDING)
                .build();

        syncOrderAndOrderItem(order, orderItems);

        orderRepository.save(order);

        saveToRedis(order);

        return OrderMapper.fromEntityToResponseDTO_CREATE(order);
    }

    @Override
    @Transactional
    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);

        orderRepository.save(order);

        return OrderMapper.fromEntityToResponseDTO_UPDATE_STATUS(order);
    }

    public List<OrderResponseDTO> getOrdersBySeat(Long seatId) {
        List<Order> orderList = orderRepository.findBySeatId(seatId);

        return orderList.stream().map(OrderMapper::fromEntityToResponseDTO).toList();


    }

    public List<OrderResponseDTO> getOrdersByStatus(List<OrderStatus> status) {
        List<Order> orderList = orderRepository.findByStatusIn(status);

        return orderList.stream().map(OrderMapper::fromEntityToResponseDTO).toList();
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

        List<String> id_list = redisOrders.stream().map(RedisOrder::getId).toList();

        List<Order> orderList = entityManager
                .createQuery("SELECT o FROM Order o WHERE o.id IN :ids", Order.class)
                .setParameter("ids", id_list)
                .getResultList();

        orderList.sort(Comparator.comparing(Order::getCreatedAt));

        return orderList.stream().map(OrderMapper::fromEntityToResponseDTO).toList();
    }

    @Override
    public void takeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        redisOrderService.delete(order.getId().toString());

        ///fixed to pending to avoid misusing this function
        order.setStatus(OrderStatus.PENDING.next());

        orderRepository.save(order);


    }

    private boolean checkAuthorization(String username) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            return true;
        }
        return false;

    }

    private User getCustomer() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info(username);
        return userService.getByUsername(username);
    }

    private void syncOrderAndOrderItem(Order order, Set<OrderItem> orderItems) {
        orderItems.forEach(i -> i.setOrder(order));
        order.setItems(orderItems);
        order.setTotalAmount(order.calculateTotal());
    }

    private void saveToRedis(Order order) {
        //redis
        try {
            String ord_id_redis = redisOrderService.save(order.getId().toString());
            log.info("Order save at redis: {}", ord_id_redis);
        } catch (Exception e) {
            log.info("Not connected to Redis");
        }
    }
}
