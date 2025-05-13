package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.request.OrderRequestDTO;
import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.*;
import com.chikacow.kohimana.repository.OrderItemRepository;
import com.chikacow.kohimana.repository.OrderRepository;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.repository.SeatRepository;
import com.chikacow.kohimana.service.OrderItemService;
import com.chikacow.kohimana.service.OrderService;
import com.chikacow.kohimana.service.SeatService;
import com.chikacow.kohimana.service.UserService;
import com.chikacow.kohimana.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final UserService userService;
    private final SeatService searService;

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

        OrderResponseDTO res = OrderResponseDTO.builder()
                .id(savedOrder.getId())
                .seatID(savedOrder.getSeat().getId())
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

    private boolean checkAuthorization(String username) {
        if (SecurityContextHolder.getContext().getAuthentication().getName().equals(username)) {
            return true;
        }
        return false;

    }
}
