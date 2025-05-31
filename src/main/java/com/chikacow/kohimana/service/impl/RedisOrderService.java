package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.redis.RedisOrder;
import com.chikacow.kohimana.repository.redis.RedisOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisOrderService {
    private final RedisOrderRepository redisOrderRepository;

    public String save(String orderId) {
        RedisOrder ord = RedisOrder.builder()
                .id(orderId)
                .expiresIn(60*60*5L)
                .build();
        var save = redisOrderRepository.save(ord);
        return save.getId();
    }

    public void delete(String orderId) {
        redisOrderRepository.deleteById(orderId);
    }

    public RedisOrder get(String orderId) {
        return redisOrderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found in redis"));
    }

    public List<RedisOrder> getAll() {
        Iterable<RedisOrder> iter = redisOrderRepository.findAll();

        List<RedisOrder> orders = new ArrayList<>();
        Iterator<RedisOrder> iterator = iter.iterator();

        while (iterator.hasNext()) {
            orders.add(iterator.next());
        }

        return orders;
    }
}
