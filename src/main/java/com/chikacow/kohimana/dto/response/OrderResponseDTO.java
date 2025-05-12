package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.util.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long seatID;
    private Long userID;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private List<OrderItemResponseDTO> items;
    private Date createdAt;
}
