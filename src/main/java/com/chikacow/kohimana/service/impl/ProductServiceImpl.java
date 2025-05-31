package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.mapper.ProductMapper;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.repository.ProductRepository;
import com.chikacow.kohimana.service.CategoryService;
import com.chikacow.kohimana.service.ProductService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.chikacow.kohimana.util.AppConst.SORT_BY;

@Service
@Slf4j(topic = "PRODUCT-SERVICE")
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final FileService fileService;

    @PersistenceContext
    private final EntityManager entityManager;


    /**
     * Create product with uploading images
     * Required upload image by the different url for file upload first
     * @param productRequestDTO
     * @return
     */
    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO) {
        Category product_s_category = getCategoryFromProductRequestDto(productRequestDTO);

        //handle image upload
        String cloudUrl = fileService.resolveImageUrl(productRequestDTO.getLocalImageUrl());

        Product newProduct = ProductMapper.fromRequestDTOToEntity(productRequestDTO, cloudUrl, product_s_category);

        entityManager.persist(newProduct);

        return ProductMapper.fromEntityToResponseDTO(newProduct);
    }

    /**
     * Get product insensitive data but enough for customer
     * @param id
     * @return
     */
    @Override
    public ProductResponseDTO getProductInfo(Long id) {
        return ProductMapper.fromEntityToResponseDTO(getProductById(id));
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
        Product retrived = getProductById(id);

        Category category = categoryService.getCategoryByCode(productRequestDTO.getCateCode());

        ProductMapper.updateEntityFromRequestDTO(retrived, productRequestDTO, category);
        /**
         * bug flush()
         */
        productRepository.save(retrived);

        return ProductMapper.fromEntityToResponseDTO(retrived);
    }


    /**
     * Delete product from the database. The file will be deleted anyway
     * @param id
     * @return
     */
    @Override
    public String changeStatus(Long id) {
        Product product = getProductById(id);
        product.setActive(!product.isActive());
        productRepository.save(product);
        return product.isActive() ? "active" : "inactive";
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
    public int createTonsProduct(List<ProductRequestDTO> productRequestDTOlist) {
        List<Product> persistedProducts = new ArrayList<>();
        for(ProductRequestDTO productRequestDTO : productRequestDTOlist) {
            Category product_s_category = getCategoryFromProductRequestDto(productRequestDTO);

            //handle image upload
            String cloudUrl = fileService.resolveImageUrl(productRequestDTO.getLocalImageUrl());

            Product newProduct = ProductMapper.fromRequestDTOToEntity(productRequestDTO, cloudUrl, product_s_category);

            entityManager.persist(newProduct);
            persistedProducts.add(newProduct);

        }
        return persistedProducts.size();
    }

    @Override
    public PageResponse<?> getAllProducts(int pageNo, int pageSize, String sortBy) {
        int realPageNo = pageNo > 0 ? pageNo -1 : 0;

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

        List<ProductResponseDTO> resList = page.stream().map(ProductMapper::fromEntityToResponseDTO).toList();

        return PageResponse.builder()
                .items(resList)
                .pageNo(realPageNo)
                .pageSize(pageSize)
                .totalPage(page.getTotalPages())
                .build();
    }


    @Override
    public List<Product> getProductListFromCodeList(List<String> codeList) {
        List<Product> products = new ArrayList<>();
        if (codeList != null) {

            for (String productCode : codeList) {
                Product product = productRepository.findByCode(productCode).orElseThrow(() -> new ResourceNotFoundException("product not found"));
                products.add(product);
            }
        }

        return products;
    }



    private List<Product> productCode2List(List<String> codes) {
        List<Product> productList = new ArrayList<>();

        for (String code : codes) {
            Product product = productRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("product not found"));
            productList.add(product);
        }
        return productList;
    }

    private Category getCategoryFromProductRequestDto(ProductRequestDTO requestDTO) {
        Category category = new Category();

        if (requestDTO.getCateCode() != null) {
            category = categoryService.getCategoryByCode(requestDTO.getCateCode());
        }

        return category;
    }

    @Override
    public List<Product> getProductsByCodes(List<String> codeList) {
        String codesCsv = String.join(",", codeList);

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("get_products_by_codes", Product.class);

        query.registerStoredProcedureParameter("codes", String.class, ParameterMode.IN);
        query.setParameter("codes", codesCsv);

        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByIds(List<Long> ids) {
        String idsCsv = String.join(",", ids.stream().map(String::valueOf).toList());

        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("get_products_by_ids", Product.class);

        query.registerStoredProcedureParameter("ids", String.class, ParameterMode.IN);
        query.setParameter("ids", idsCsv);

        return query.getResultList();
    }


}
