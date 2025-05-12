package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.util.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderItemResponseDTO {
    private Long id;
    private Long productID;
    private Integer quantity;
    private BigDecimal price;
    private String note;
}
