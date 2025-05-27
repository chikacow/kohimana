package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.response.OrderResponseDTO;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.service.OrderService;
import com.chikacow.kohimana.util.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public ResponseData<?> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        var res = orderService.updateOrderStatus(orderId, status);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    /**
     * checked
     * @param seatId
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    @GetMapping("/seat/{seatId}")
    public ResponseData<?> getOrdersBySeat(
            @PathVariable Long seatId) {
        var res = orderService.getOrdersBySeat(seatId);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    /**
     * checked
     * @param status
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    @GetMapping("/status")
    public ResponseData<?> getOrdersByStatus(
            @RequestParam List<OrderStatus> status ) {
        var res = orderService.getOrdersByStatus(status);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    /**
     * for staff to check for incoming orders
     * @param
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER', 'STAFF')")
    @GetMapping("/get-latest")
    public ResponseData<?> getLatestPendingOrders() {
        var res = orderService.getLatestOrders();
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }
}
