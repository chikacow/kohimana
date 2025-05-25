package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.request.CategoryRequestDTO;
import com.chikacow.kohimana.dto.response.CategoryResponseDTO;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/category")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminCategoryController {
    private CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseData<?> createNewCategory(@Valid @RequestBody CategoryRequestDTO requestDTO) {

        CategoryResponseDTO res = categoryService.createCategory(requestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    /**
     * Coming soon in next version
     */

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseData<?> getCategoryInfo(@PathVariable("id") Long id) {

        CategoryResponseDTO res = categoryService.getCategoryInfo(id);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{id}")
    public ResponseData<?> updateCategoryInfo(@PathVariable("id") Long id, @Valid @RequestBody CategoryRequestDTO requestDTO) {

        CategoryResponseDTO res = categoryService.updateCategoryInfo(id, requestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    /**
     * Use to hide or display category, can work as delete category
     * @param id
     * @return
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PatchMapping("/{id}")
    public ResponseData<?> changeCategoryStatus(@PathVariable("id") Long id) {
        String status = categoryService.changeStatus(id);
        Map<String, String> res = new HashMap<>();
        res.put("status", status);
        res.put("cate_id", "id");


        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }
}
