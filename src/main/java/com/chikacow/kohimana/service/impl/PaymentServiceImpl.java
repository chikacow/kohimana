package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.PaymentRequestDTO;
import com.chikacow.kohimana.dto.response.PaymentResponseDTO;
import com.chikacow.kohimana.exception.HaveNoAccessToResourceException;
import com.chikacow.kohimana.exception.PaymentException;
import com.chikacow.kohimana.mapper.PaymentMapper;
import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.model.Payment;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.OrderRepository;
import com.chikacow.kohimana.repository.PaymentRepository;
import com.chikacow.kohimana.service.OrderService;
import com.chikacow.kohimana.service.PaymentService;
import com.chikacow.kohimana.util.enums.OrderStatus;
import jdk.jfr.Registered;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponseDTO processPayment(PaymentRequestDTO requestDTO) {

        Order order = orderService.getOrderById(requestDTO.getOrderID());
        checkOrderConstraints(order);

        Payment payment = PaymentMapper.fromRequestDTOToEntity(requestDTO, order);
        checkPaymentConstraints(payment, order);

        paymentRepository.save(payment);

        //update order status
        syncWithOrderStatus(order);

        ///check list payment in order
        List<Payment> llgg = order.getPaymentList();
        log.info(Integer.toString(llgg.size()));

        return PaymentMapper.fromEntityToResponseDTO(paymentRepository.save(payment));
    }

    /**
     * next version
     * @param transactionId
     * @return
     */
    @Override
    public PaymentResponseDTO checkPaymentStatus(String transactionId) {
        return null;
    }

    /**
     * next version
     * @param transactionId
     * @return
     */
    @Override
    public PaymentResponseDTO refundPayment(String transactionId) {
        return null;
    }

    private void checkOrderConstraints(Order order) {
        /**
         * assure the order must be paid by its owner
         */
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        String userMakeOrder = order.getUser().getUsername();
        if (!currentUser.equals(userMakeOrder)) {
            throw new HaveNoAccessToResourceException("ko duoc thanh toan cho nhau (in this version)");
        }

        if (order.getStatus().equals(OrderStatus.PAID)) {
            throw new PaymentException("This order is already fully paid");
        }

        if (!order.getStatus().equals(OrderStatus.SERVED)) {
            throw new PaymentException("This order is not ready to be paid, must be SERVED first!");
        }
    }

    private void checkPaymentConstraints(Payment payment, Order order) {
        if (payment.getAmount().compareTo(order.getTotalAmount()) < 0) {
            throw new PaymentException("You do not have enough money!");
        }
    }

    private void syncWithOrderStatus(Order order) {
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
    }
}
