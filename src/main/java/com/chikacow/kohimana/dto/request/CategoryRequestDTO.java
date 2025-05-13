package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.util.enums.CategoryType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "category id must not be blank")
    private String categoryID;

    @NotBlank(message = "category name must not be blank")
    private String name;

    @NotBlank(message = "must specify category type")
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    /**
     * digging for further utilities
     */
    private List<String> productCodes;
}
