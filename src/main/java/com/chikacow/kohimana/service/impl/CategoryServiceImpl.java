package com.chikacow.kohimana.service.impl;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.exception.ResourceNotFoundException;
import com.chikacow.kohimana.mapper.CategoryMapper;
import com.chikacow.kohimana.model.Category;
import com.chikacow.kohimana.model.Product;
import com.chikacow.kohimana.repository.CategoryRepository;
import com.chikacow.kohimana.service.CategoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j(topic = "CATEGORY-SERVICE")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("category not found"));
    }

    @Override
    public Category getCategoryByCode(String code) {
        return categoryRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("cate not found by code"));
    }

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
        //List<Product> products = getProductListFromCodeList(requestDTO.getProductCodes());
        List<Product> products = getProductsByCodes(requestDTO.getProductCodes());
        Category category = CategoryMapper.fromRequestDTOToEntity(requestDTO, products);
        updateCategoryInProductList(category);

        ///vì là tham chiếu nên khi id trong category đc cập nhật là giá trị cate id trong product id cx đc cập nhật theo luôn
        /// nhớ rằng đây là tham chiếu, pass by reference chứ ko phải pass by value nhé
        entityManager.persist(category);
        entityManager.flush();

        return CategoryMapper.fromEntityToResponseDTO(category);
    }

    @Override
    public CategoryResponseDTO getCategoryInfo(Long id) {
        return CategoryMapper.fromEntityToResponseDTO(getCategoryById(id));
    }

    /**
     *Must satisfy that all the update data, including existed product code, to perform updating
     */
    @Override
    @Transactional
    public CategoryResponseDTO updateCategoryInfo(Long id, CategoryRequestDTO requestDTO) {
        Category retrived = getCategoryById(id);

        CategoryMapper.updateEntityFromRequestDTO(retrived, requestDTO);
        Category saved = categoryRepository.save(retrived);

        return CategoryMapper.fromEntityToResponseDTO(saved);
    }

    @Override
    @Transactional
    public String changeStatus(Long id) {
        Category retrived = getCategoryById(id);
        //entityManager.detach(retrived);

        retrived.setActive(!retrived.isActive());

        //entityManager.merge(retrived);
        //categoryRepository.save(retrived);

        return retrived.isActive() ? "active" : "inactive";
    }




    private void updateCategoryInProductList(Category category) {
        List<Product> products = category.getProductList();
        if (products != null) {
            products.forEach(p -> p.setCategory(category));
        }

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
}


