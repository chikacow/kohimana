package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.response.OrderItemResponseDTO;
import com.chikacow.kohimana.model.OrderItem;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.repository.OrderItemRepository;
import com.chikacow.kohimana.repository.OrderRepository;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.repository.UserRepository;
import com.chikacow.kohimana.service.OrderItemService;
import com.chikacow.kohimana.service.OrderService;
import com.chikacow.kohimana.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    private final ProductService productService;

    @Override
    public OrderItemResponseDTO addItemToOrder(Long orderId, OrderItemRequestDTO request) {
        return null;
    }

    @Override
    public OrderItemResponseDTO updateOrderItem(Long itemId, OrderItemRequestDTO request) {
        return null;
    }

    @Override
    public void removeItemFromOrder(Long itemId) {

    }

    @Override
    public List<OrderItemResponseDTO> getItemsByOrder(Long orderId) {
        return List.of();
    }

    /**
     * next version will have detect duplicate records and group them
     * @param requests
     * @return
     */
    @Transactional
    @Override
    public Set<OrderItem> createOrderItemsFromDTO(List<OrderItemRequestDTO> requests) {
        Set<OrderItem> orderItemSet = new HashSet<>();
        for (OrderItemRequestDTO req : requests) {
            Product product = productService.getProductById(req.getProductID());
            OrderItem orderItem = OrderItem.builder()
                    .note(req.getNote())
                    .price(product.getPrice())
                    .quantity(req.getQuantity())
                    .product(product)
                    .build();
            orderItemSet.add(orderItem);
        }


        return orderItemSet;
    }

    @Override
    public List<OrderItemResponseDTO> convertToDTOs(Set<OrderItem> items) {
        List<OrderItemResponseDTO> res = new ArrayList<>();
        for (OrderItem it : items) {
            OrderItemResponseDTO reis = OrderItemResponseDTO.builder()
                    .note(it.getNote())
                    .price(it.getPrice())
                    .quantity(it.getQuantity())
                    .id(it.getId())
                    .productID(it.getProduct().getId())
                    .build();

            res.add(reis);
        }
        return res;
    }
}
