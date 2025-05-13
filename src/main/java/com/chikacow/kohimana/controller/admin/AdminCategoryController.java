package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/category")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<CategoryResponseDTO> createNewCategory(@RequestBody CategoryRequestDTO requestDTO) {

        CategoryResponseDTO res = categoryService.createCategory(requestDTO);

        return ResponseEntity.ok(res);
    }

    /**
     * Coming soon in next version
     */

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return null;
    }

    @GetMapping("/{categoryID}")
    public ResponseEntity<CategoryResponseDTO> getCategoryInfo(@PathVariable("categoryID") String categoryID) {

        CategoryResponseDTO res = categoryService.getCategoryInfo(categoryID);

        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{categoryID}")
    public ResponseEntity<CategoryResponseDTO> updateCategoryInfo(@PathVariable("categoryID") String categoryID, @RequestBody CategoryRequestDTO requestDTO) {

        CategoryResponseDTO res = categoryService.updateCategoryInfo(categoryID, requestDTO);

        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{categoryID}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryID") String categoryID) {
        String cateId = categoryService.deleteCategory(categoryID);
        return ResponseEntity.ok(cateId);
    }
}
