package com.chikacow.kohimana.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderItemRequestDTO {
    @NotNull(message = "product id is required")
    private Long productID;

    @Min(value = 1, message = "quantity stays at least 1")
    private Integer quantity;

    private String note;
}
