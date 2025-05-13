package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.MoneyCurrency;
import com.chikacow.kohimana.util.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentResponseDTO {

    private String transactionId;

    private BigDecimal amount = BigDecimal.ZERO;

    private Long orderId;

    private String username;

    private PaymentMethod paymentMethod;

    private String description;

    private MoneyCurrency currency;

    private BigDecimal charge;
}
