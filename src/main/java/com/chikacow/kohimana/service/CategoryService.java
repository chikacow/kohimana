package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.model.Category;

public interface CategoryService {
    public Category getCategoryById(Long id);
    public Category getCategoryByCode(String code);

    public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    public CategoryResponseDTO getCategoryInfo(String categoryID);

    public CategoryResponseDTO updateCategoryInfo(String categoryID, CategoryRequestDTO requestDTO);

    public String deleteCategory(String categoryID);
}
