package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.dto.response.UserResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.model.User;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.service.CategoryService;
import com.chikacow.kohimana.service.FileService;
import com.chikacow.kohimana.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chikacow.kohimana.util.AppConst.SORT_BY;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    private final FileService fileService;


    /**
     * Create product with uploading images
     * Required upload image by the different url for file upload first
     * @param productRequestDTO
     * @return
     */
    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {

        Category category = new Category();
        if (productRequestDTO.getCateCode() != null) {
            category = categoryService.getCategoryByCode(productRequestDTO.getCateCode());
        }

        String cloudUrl = resolveImageUrl(productRequestDTO.getLocalImageUrl());

        Product newProduct = Product.builder()
                .code(productRequestDTO.getCode())
                .name(productRequestDTO.getName())
                .description(productRequestDTO.getDescription())
                .price(productRequestDTO.getPrice())
                .imageUrl(cloudUrl)
                .category(category)
                .build();

        Product savedProduct = productRepository.save(newProduct);

        ProductResponseDTO res = ProductResponseDTO.builder()
                .code(savedProduct.getCode())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .imageUrl(savedProduct.getImageUrl())
                .categoryID(savedProduct.getCategory() != null ? savedProduct.getCategory().getCode() : "none")
                .build();

        return res;
    }

    /**
     * Get product insensitive data but enough for customer
     * @param id
     * @return
     */
    @Override
    public ProductResponseDTO getProductInfo(Long id) {
        Product retrived = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product not found"));

        ProductResponseDTO res = ProductResponseDTO.builder()
                .code(retrived.getCode())
                .name(retrived.getName())
                .description(retrived.getDescription())
                .price(retrived.getPrice())
                .imageUrl(retrived.getImageUrl())
                .categoryID(retrived.getCategory() != null ? retrived.getCategory().getCode() : "none")
                .build();
        return res;
    }

    /**
     * For manager to update product's data
     * @param id
     * @param productRequestDTO
     * @return
     */
    @Override
    @Transactional
    public ProductResponseDTO updateProductInfo(Long id, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product not found"));

        if (productRequestDTO.getCode() != null) {
            product.setCode(productRequestDTO.getCode());
        }
        if (productRequestDTO.getName() != null) {
            product.setName(productRequestDTO.getName());
        }
        if (productRequestDTO.getDescription() != null) {
            product.setDescription(productRequestDTO.getDescription());
        }
        if (productRequestDTO.getPrice().longValue() != 0) {
            product.setPrice(productRequestDTO.getPrice());
        }
        if (productRequestDTO.getLocalImageUrl() != null && !resolveImageUrl(productRequestDTO.getLocalImageUrl()).equals("not found") ) {
            product.setImageUrl(resolveImageUrl(productRequestDTO.getLocalImageUrl()));
        }
        if (productRequestDTO.getCateCode() != null) {
            product.setCategory(categoryService.getCategoryByCode(productRequestDTO.getCateCode()));
        }

        Product savedProduct = productRepository.save(product);

        ProductResponseDTO res = ProductResponseDTO.builder()
                .code(savedProduct.getCode())
                .name(savedProduct.getName())
                .description(savedProduct.getDescription())
                .price(savedProduct.getPrice())
                .imageUrl(savedProduct.getImageUrl())
                .categoryID(savedProduct.getCategory() != null ? savedProduct.getCategory().getCode() : "none")
                .build();
        return res;
    }

    /**
     * Delete product from the database. The file will be deleted anyway
     * @param id
     * @return
     */
    @Override
    public Long deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product not found"));
        productRepository.delete(product);
        return product.getId();
    }

    /**
     * Get product by id, only return 1 records
     * @param id
     * @return
     */
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("product not found"));
    }


    @Override
    @Transactional
    public ProductResponseDTO createTonsProduct(List<ProductRequestDTO> productRequestDTOlist) {

        for(ProductRequestDTO productRequestDTO : productRequestDTOlist) {
            Category category = new Category();
            if (productRequestDTO.getCateCode() != null) {
                category = categoryService.getCategoryByCode(productRequestDTO.getCateCode());
            }


            String cloudUrl = resolveImageUrl(productRequestDTO.getLocalImageUrl());


            Product newProduct = Product.builder()
                    .code(productRequestDTO.getCode())
                    .name(productRequestDTO.getName())
                    .description(productRequestDTO.getDescription())
                    .price(productRequestDTO.getPrice())
                    .imageUrl(cloudUrl)
                    .category(category)
                    .build();

            Product savedProduct = productRepository.save(newProduct);


        }

        return null;
    }

    @Override
    public PageResponse<?> getAllProducts(int pageNo, int pageSize, String sortBy) {
        int realPageNo = 0;
        if (pageNo > 0) {
            realPageNo = pageNo - 1;
        }
        List<Sort.Order> sorts = new ArrayList<>();
        if (StringUtils.hasLength(sortBy)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile(SORT_BY);
            Matcher matcher = pattern.matcher(sortBy);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    sorts.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    sorts.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }
        Pageable pageable = PageRequest.of(realPageNo, pageSize, Sort.by(sorts));

        Page<Product> page = productRepository.findAll(pageable);

        List<ProductResponseDTO> resList = page.stream().map(prod -> ProductResponseDTO.builder()
                .code(prod.getCode())
                .name(prod.getName())
                .description(prod.getDescription())
                .price(prod.getPrice())
                .imageUrl(prod.getImageUrl())
                .categoryID(prod.getCategory() != null ? prod.getCategory().getCode() : "none")
                .build()).toList();



        return PageResponse.builder()
                .items(resList)
                .pageNo(realPageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .build();
    }




    /**
     * handle the file upload
     * @param localUrl
     * @return
     */
    private String resolveImageUrl(String localUrl) {
        String rt = "not found";
        try {
            if (localUrl != null) {
                return fileService.getCloudUrl(localUrl);
            } else {
                return "no image";
            }
        } catch (Exception e) {
            log.info("Failed to resolve image url");
        }

        return rt;
    }
}
