package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.List;
import java.util.function.Consumer;

public class ProductMapper implements DTOMapper<Product> {
    public static Product fromRequestDTOToEntity(ProductRequestDTO requestDTO, String imageUrl, Category category) {
        return Product.builder()
                .code(requestDTO.getCode())
                .name(requestDTO.getName())
                .description(requestDTO.getDescription())
                .price(requestDTO.getPrice())
                .imageUrl(imageUrl)
                .category(category)
                .build();



    }


    public static ProductResponseDTO fromEntityToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .categoryID(product.getCategory() != null ? product.getCategory().getCode() : "none")
                .build();


    }

    public static void updateEntityFromRequestDTO(Product product, ProductRequestDTO requestDTO, Category category) {



        applyIfNotNull(requestDTO.getCode(), product::setCode);
        applyIfNotNull(requestDTO.getName(), product::setName);
        applyIfNotNull(requestDTO.getDescription(), product::setDescription);
        applyIfNotNull(requestDTO.getPrice(), product::setPrice);
        applyIfNotNull(requestDTO.getLocalImageUrl(), product::setImageUrl);
        applyIfNotNull(category, product::setCategory);


    }

    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static void normalizeRequestDTO(CategoryRequestDTO requestDTO) {

    }
}
