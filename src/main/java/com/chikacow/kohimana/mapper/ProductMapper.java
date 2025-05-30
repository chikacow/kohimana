package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.List;
import java.util.function.Consumer;

public class ProductMapper implements DTOMapper<Product> {
    public static Category fromRequestDTOToEntity(CategoryRequestDTO requestDTO, List<Product> products) {


    }


    public static CategoryResponseDTO fromEntityToResponseDTO(Category category) {


    }

    public static void updateUserFromRequestDTO(Category category, CategoryRequestDTO requestDTO) {


        applyIfNotNull(requestDTO.getCode(), category::setCode);
        applyIfNotNull(requestDTO.getName(), category::setName);
        applyIfNotNull(CategoryType.fromString(requestDTO.getType()), category::setType);

    }

    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static void normalizeRequestDTO(CategoryRequestDTO requestDTO) {

    }
}
