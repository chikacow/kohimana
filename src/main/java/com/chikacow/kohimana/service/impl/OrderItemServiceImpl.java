package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.OrderItemRequestDTO;
import com.chikacow.kohimana.dto.response.OrderItemResponseDTO;
import com.chikacow.kohimana.mapper.OrderItemMapper;
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
import java.util.*;

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
        List<Long> productIds = getProductIdsFromOrderItemList(requests);
        List<Product> productList = productService.getProductsByIds(productIds);

        Set<OrderItem> orderItemSet = new HashSet<>();
        for (int i = 0; i < requests.size(); i++) {
            Product product = productList.get(i);
            orderItemSet.add(OrderItemMapper.fromRequestDTOToEntity(requests.get(i), product));
        }

        return orderItemSet;
    }



    private List<Long> getProductIdsFromOrderItemList(List<OrderItemRequestDTO> requests) {
        List<Long> productIds = new LinkedList<>();
        for (OrderItemRequestDTO req : requests) {
            productIds.add(req.getProductID());
        }
        return productIds;
    }
}
