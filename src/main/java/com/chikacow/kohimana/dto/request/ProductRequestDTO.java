package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class ProductRequestDTO {
    @NotBlank(message = "Must include product code")
    private String code;

    @NotBlank(message = "Must include product name")
    private String name;

    @Min(value = 0, message = "Default price will be 0")
    private BigDecimal price;

    private String description;

    @NotBlank(message = "Must include category code")
    private String cateCode;

    private String localImageUrl;

}
