package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.validator.EnumSubset;
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

    @NotBlank(message = "Category id must not be blank")
    private String categoryID;

    @NotBlank(message = "Category name must not be blank")
    private String name;

    @EnumSubset(enumClass = CategoryType.class)
    private String type;

    /**
     * digging for further utilities
     */
    private List<String> productCodes;
}
