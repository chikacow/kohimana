package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductRequestDTO {
    private String code;

    private String name;

    private double price;

    private String description;

    private String cateCode;

    private String localImageUrl;

}
