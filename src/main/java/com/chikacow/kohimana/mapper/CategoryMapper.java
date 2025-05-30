package com.chikacow.kohimana.mapper;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.util.enums.CategoryType;
import com.chikacow.kohimana.util.enums.Gender;
import com.chikacow.kohimana.util.helper.SmoothData;

import java.util.List;
import java.util.function.Consumer;

public class CategoryMapper implements DTOMapper<Category> {
    public static Category fromRequestDTOToEntity(CategoryRequestDTO requestDTO, List<Product> products) {
        normalizeRequestDTO(requestDTO);
        return Category.builder()
                .code(requestDTO.getCode())
                .name(requestDTO.getName())
                .type(CategoryType.fromString(requestDTO.getType()))
                .productList(products)
                .isActive(true)
                .build();

    }


    public static CategoryResponseDTO fromEntityToResponseDTO(Category category) {
        return CategoryResponseDTO.builder()
                .categoryID(category.getCode())
                .name(category.getName())
                .type(category.getType())
                .productCodes(productList2Code(category.getProductList()))
                .build();

    }

    public static void updateUserFromRequestDTO(Category category, CategoryRequestDTO requestDTO) {
        normalizeRequestDTO(requestDTO);

        applyIfNotNull(requestDTO.getCode(), category::setCode);
        applyIfNotNull(requestDTO.getName(), category::setName);
        applyIfNotNull(CategoryType.fromString(requestDTO.getType()), category::setType);
        //        if (requestDTO.getProductCodes() != null) {
        //            log.info("must include all product code before to avoid data lost");
        //            retrived.getProductList().remove(0);
        //            //phai save thi orphan ms hoat dong, chung to hibernate khong the anh xa live
        //            //categoryRepository.save(retrived);
        //            retrived.setProductList(productCode2List(requestDTO.getProductCodes()));
        //        }

    }

    private static <T> void applyIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private static void normalizeRequestDTO(CategoryRequestDTO requestDTO) {

        requestDTO.setName(SmoothData.smooth(requestDTO.getName()));
    }

    private static List<String> productList2Code(List<Product> products) {
        return products.stream().map(Product::getCode).toList();
    }




}
