package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.Order;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.util.enums.MoneyCurrency;
import com.chikacow.kohimana.util.enums.PaymentMethod;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PaymentRequestDTO {

    @NotNull
    private BigDecimal amount = BigDecimal.ZERO;

    @NotNull
    private Long orderID;

    //@NotBlank cnay chi dc phep dung cho String, vs enum se bi loi
    @NotNull
    private PaymentMethod paymentMethod;

    private String description;

    @NotNull
    private MoneyCurrency currency;

}
