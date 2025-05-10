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

    @NotBlank
    private String categoryID;

    @NotBlank
    private String name;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private List<String> productCodes;
}
