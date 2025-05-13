package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.request.PaymentRequestDTO;
import com.chikacow.kohimana.dto.response.PaymentResponseDTO;
import com.chikacow.kohimana.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    /**
     *orderID be kept in request body
     * @param requestDTO
     * @return
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDTO> processPayment(@Valid @RequestBody PaymentRequestDTO requestDTO) {
        PaymentResponseDTO response = paymentService.processPayment(requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * next version
     * @param transactionId
     * @return
     */
    //@GetMapping("/{transactionId}")
    public ResponseEntity<PaymentResponseDTO> checkPaymentStatus(@PathVariable String transactionId) {
        PaymentResponseDTO response = paymentService.checkPaymentStatus(transactionId);
        return ResponseEntity.ok(response);
    }

    /**
     * next version
     * @param transactionId
     * @return
     */
    //@PostMapping("/{transactionId}/refund")
    public ResponseEntity<PaymentResponseDTO> refundPayment(@PathVariable String transactionId) {
        PaymentResponseDTO response = paymentService.refundPayment(transactionId);
        return ResponseEntity.ok(response);
    }
}
