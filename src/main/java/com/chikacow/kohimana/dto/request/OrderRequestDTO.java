package com.chikacow.kohimana.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class OrderRequestDTO {
    @NotNull(message = "Seat ID is required")
    private Long seatID;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequestDTO> items;
}
