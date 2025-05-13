package com.chikacow.kohimana.repository;

import com.chikacow.kohimana.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
