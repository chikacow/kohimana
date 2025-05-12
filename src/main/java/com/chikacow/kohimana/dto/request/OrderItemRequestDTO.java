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
    @NotNull(message = "Product ID is required")
    private Long productID;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String note;
}
