package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.repository.CategoryRepository;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;


    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category not found"));
    }

    @Override
    public Category getCategoryByCode(String code) {
        return categoryRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("cate not found by code"));
    }

    @Override
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {

        List<Product> products = new ArrayList<>();
        if (requestDTO.getProductCodes() != null) {
            List<String> productCodes = requestDTO.getProductCodes();

            for (String productCode : productCodes) {
                Product product = productRepository.findByCode(productCode).orElseThrow(() -> new ResourceNotFoundException("product not found"));
                products.add(product);
            }
        }


        Category category = Category.builder()
                .code(requestDTO.getCategoryID())
                .name(requestDTO.getName())
                .type(requestDTO.getType())
                .productList(products)
                .build();

        Category newCate = categoryRepository.save(category);



        CategoryResponseDTO res = CategoryResponseDTO.builder()
                .categoryID(newCate.getCode())
                .name(newCate.getName())
                .type(newCate.getType())
                //.productCodes()
                .build();

        return res;
    }

    @Override
    public CategoryResponseDTO getCategoryInfo(String categoryID) {

        Category retrived = categoryRepository.findByCode(categoryID).orElseThrow(() -> new ResourceNotFoundException("category not found"));

        CategoryResponseDTO res = CategoryResponseDTO.builder()
                .categoryID(retrived.getCode())
                .name(retrived.getName())
                .type(retrived.getType())
                .productCodes(productList2Code(retrived.getProductList()))
                .build();

        return res;
    }

    /**
     *Must satisfy that all the update data, including existed product code, to perform updating
     */
    @Override
    public CategoryResponseDTO updateCategoryInfo(String categoryID, CategoryRequestDTO requestDTO) {

        Category retrived = categoryRepository.findByCode(categoryID).orElseThrow(() -> new ResourceNotFoundException("category not found"));

        if (requestDTO.getCategoryID() != null) {
            retrived.setCode(requestDTO.getCategoryID());
        }
        if (requestDTO.getName() != null) {
            retrived.setName(requestDTO.getName());
        }
        if (requestDTO.getType() != null) {
            retrived.setType(requestDTO.getType());
        }
        if (requestDTO.getProductCodes() != null) {
            log.info("must include all product code before to avoid data lost");
            retrived.setProductList(productCode2List(requestDTO.getProductCodes()));
        }

        Category saved = categoryRepository.save(retrived);

        CategoryResponseDTO res = CategoryResponseDTO.builder()
                .categoryID(saved.getCode())
                .name(saved.getName())
                .type(saved.getType())
                .productCodes(productList2Code(retrived.getProductList()))
                .build();

        return res;
    }

    @Override
    public String deleteCategory(String categoryID) {
        Category retrived = categoryRepository.findByCode(categoryID).orElseThrow(() -> new ResourceNotFoundException("category not found, which means you dont have to delete it"));

        categoryRepository.delete(retrived);

        return retrived.getCode();
    }


    private List<String> productList2Code(List<Product> products) {
        List<String> productCodes = new ArrayList<>();

        for (Product product : products) {
            productCodes.add(product.getCode());
        }
        return productCodes;
    }

    private List<Product> productCode2List(List<String> codes) {
        List<Product> productList = new ArrayList<>();

        for (String code : codes) {
            Product product = productRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("product not found"));
            productList.add(product);
        }
        return productList;
    }
}
