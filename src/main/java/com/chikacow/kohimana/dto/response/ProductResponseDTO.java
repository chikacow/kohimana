package com.chikacow.kohimana.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductResponseDTO {
    private String code;

    private String name;

    private BigDecimal price;

    private String description;

    private String imageUrl;

    private String categoryID;

}
