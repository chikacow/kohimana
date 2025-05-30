package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.dto.response.OrderItemResponseDTO;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.OrderItem;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class OrderItemMapper implements DTOMapper<OrderItem> {
    public static OrderItem fromRequestDTOToEntity(OrderItemRequestDTO requestDTO, Product product) {
        return OrderItem.builder()
                .note(requestDTO.getNote())
                .price(product.getPrice())
                .quantity(requestDTO.getQuantity())
                .product(product)
                .build();
    }


    public static OrderItemResponseDTO fromEntityToResponseDTO(OrderItem orderItem) {
        return OrderItemResponseDTO.builder()
                .note(orderItem.getNote())
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .id(orderItem.getId())
                .productID(orderItem.getProduct().getId())
                .build();

    }

    public static void updateEntityFromRequestDTO(Category category, CategoryRequestDTO requestDTO) {
        normalizeRequestDTO(requestDTO);

        applyIfNotNull(requestDTO.getCode(), category::setCode);
        applyIfNotNull(requestDTO.getName(), category::setName);
        applyIfNotNull(CategoryType.fromString(requestDTO.getType()), category::setType);


    }

    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static void normalizeRequestDTO(CategoryRequestDTO requestDTO) {

        requestDTO.setName(SmoothData.smooth(requestDTO.getName()));
    }


    public static List<OrderItemResponseDTO> convertToDTOs(Set<OrderItem> items) {
        //items.stream().map(OrderItemMapper::fromEntityToResponseDTO).forEach(res::add);
        return items.stream().map(OrderItemMapper::fromEntityToResponseDTO).toList();

    }
}
