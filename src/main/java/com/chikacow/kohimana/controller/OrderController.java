package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.OrderRequestDTO;
import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.service.OrderService;
import com.chikacow.kohimana.util.enums.OrderStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
@Validated
@Slf4j
public class OrderController {
    private final OrderService orderService;

    /**
     * used when customer select product. once they send this API, the order will be pending and will be sent
     * to the staff. If they regret, they can cancel the order
     * They have to send the correct productid, otherwise an exception will be thrown
     * @param request
     * @return
     */
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderResponseDTO response = orderService.createOrder(request);
        return ResponseEntity.ok(response);
    }







}
