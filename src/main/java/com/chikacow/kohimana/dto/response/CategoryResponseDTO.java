package com.chikacow.kohimana.dto.response;

import com.chikacow.kohimana.util.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponseDTO {

    private String categoryID;

    private String name;

    @Enumerated(EnumType.STRING)
    private CategoryType type;

    private List<String> productCodes;

}
