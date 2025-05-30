package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.util.enums.OrderStatus;

public class OrderMapper implements DTOMapper<Order> {

    public static OrderResponseDTO fromEntityToResponseDTO_CREATE(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .seatID(order.getSeat().getId())
                .userID(order.getUser().getId())
                .items(OrderItemMapper.convertToDTOs(order.getItems()))
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .status(OrderStatus.PENDING)
                .build();

    }
    public static OrderResponseDTO fromEntityToResponseDTO_UPDATE_STATUS(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .userID(order.getUser().getId())
                .build();
    }

    public static OrderResponseDTO fromEntityToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .userID(order.getUser().getId())
                .build();


    }
}
