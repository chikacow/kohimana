package com.chikacow.kohimana.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductResponseDTO {
    private String code;

    private String name;

    private double price;

    private String description;

    private String imageUrl;

    private String categoryID;

}
