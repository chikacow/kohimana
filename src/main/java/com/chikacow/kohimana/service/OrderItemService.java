package com.chikacow.kohimana.service;


import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.response.OrderItemResponseDTO;
import com.chikacow.kohimana.model.OrderItem;

import java.util.List;
import java.util.Set;

public interface OrderItemService {

    public OrderItemResponseDTO addItemToOrder(Long orderId, OrderItemRequestDTO request);

    public OrderItemResponseDTO updateOrderItem(Long itemId, OrderItemRequestDTO request);

    public void removeItemFromOrder(Long itemId);

    public List<OrderItemResponseDTO> getItemsByOrder(Long orderId);

    public Set<OrderItem> createOrderItemsFromDTO(List<OrderItemRequestDTO> requests);

    public List<OrderItemResponseDTO> convertToDTOs(Set<OrderItem> items);
}