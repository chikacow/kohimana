package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.OrderRequestDTO;
import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.model.redis.RedisOrder;
import com.chikacow.kohimana.util.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    public OrderResponseDTO createOrder(OrderRequestDTO request);

    public OrderResponseDTO updateOrderStatus(Long orderId, OrderStatus status);

    public List<OrderResponseDTO> getOrdersBySeat(Long seatId);

    public List<OrderResponseDTO> getOrdersByStatus(List<OrderStatus> status);

    public Order getOrderById(Long orderId);

    public List<OrderResponseDTO> getLatestOrders();

    public void takeOrder(Long orderId);

}
