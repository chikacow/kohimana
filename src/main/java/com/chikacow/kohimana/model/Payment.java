package com.chikacow.kohimana.model;

import com.chikacow.kohimana.model.listener.PaymentListener;
import com.chikacow.kohimana.util.enums.MoneyCurrency;
import com.chikacow.kohimana.util.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Entity
@EntityListeners(PaymentListener.class) // Tự động gọi listener
@Table(name = "tbl_payment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends AbstractEntity<Long> {

    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(name = "charge")
    private BigDecimal charge = BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id") // Đảm bảo khi xoá order thì user vẫn còn để sau còn audit
    private User user;


    @Column(name = "method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "description")
    private String description;

    @Column
    @Enumerated(EnumType.STRING)
    private MoneyCurrency currency;

    //set order to user automatically
    @PrePersist
    public void prePersist() {
        if (this.order != null && this.user == null) {
            this.user = this.order.getUser(); // Đồng bộ user từ Order
        }
        generateTransactionId();
    }
    @PreUpdate
    public void preUpdate() {
        if (this.order != null) {
            this.user = this.order.getUser();
        }
    }

    /**
     * generate transactionID function
     */
    private void generateTransactionId() {
        long timestamp = Instant.now().toEpochMilli();
        int randomNum = ThreadLocalRandom.current().nextInt(1000, 9999);
        String rt =  "TXN-" + timestamp + "-" + randomNum;
        this.transactionId = rt;
    }


}

