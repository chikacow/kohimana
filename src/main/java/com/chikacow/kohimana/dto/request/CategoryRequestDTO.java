package com.chikacow.kohimana.dto.request;

import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.validator.EnumSubset;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "Category code must not be blank")
    private String code;

    @NotBlank(message = "Category name must not be blank")
    private String name;

    @EnumSubset(enumClass = CategoryType.class)
    private String type;

    /**
     * digging for further utilities
     */
    private List<String> productCodes;
}
