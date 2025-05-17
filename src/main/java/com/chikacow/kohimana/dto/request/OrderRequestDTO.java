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

    private Long seatID = 0L;

    @NotEmpty(message = "must include some order items")
    private List<OrderItemRequestDTO> items;
}
