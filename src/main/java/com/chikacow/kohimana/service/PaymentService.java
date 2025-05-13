package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.PaymentRequestDTO;
import com.chikacow.kohimana.dto.response.PaymentResponseDTO;

public interface PaymentService {

    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO);


    public PaymentResponseDTO checkPaymentStatus(String transactionId);


    public PaymentResponseDTO refundPayment(String transactionId);
}
