package com.chikacow.kohimana.model.listener;

import com.chikacow.kohimana.model.Payment;
import jakarta.persistence.PrePersist;

// Class listener riêng
public class PaymentListener {
    //@PrePersist
    public void syncUser(Payment payment) {
        if (payment.getOrder() != null) {
            payment.setUser(payment.getOrder().getUser());
        }
    }
}