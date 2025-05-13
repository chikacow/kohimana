package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.service.OrderService;
import com.chikacow.kohimana.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/order")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminOrderController {
    private final OrderService orderService;
    /**
     * Checked
     * @param orderId
     * @param status
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }

    /**
     * checked
     * @param seatId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    @GetMapping("/seat/{seatId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersBySeat(
            @PathVariable Long seatId) {
        return ResponseEntity.ok(orderService.getOrdersBySeat(seatId));
    }

    /**
     * checked
     * @param status
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    @GetMapping("/status")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByStatus(
            @RequestParam List<OrderStatus> status ) {
        return ResponseEntity.ok(orderService.getOrdersByStatus(status));
    }
}
