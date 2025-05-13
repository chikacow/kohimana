package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.service.CategoryService;
import com.chikacow.kohimana.service.FileService;
import com.chikacow.kohimana.service.MinioService;
import com.chikacow.kohimana.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/product")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<ProductResponseDTO> createNewProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO res = productService.createProduct(productRequestDTO);

        return ResponseEntity.ok(res);
    }

//    @PostMapping("/create")
//    public ResponseEntity<ProductResponseDTO> createNewProduct(@RequestBody List<ProductRequestDTO> productRequestDTO) {
//        ProductResponseDTO res = productService.createTonsProduct(productRequestDTO);
//
//        return ResponseEntity.ok(res);
//    }



    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO res = productService.updateProductInfo(productId, productRequestDTO);

        return ResponseEntity.ok(res);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Long> deleteProduct(@PathVariable Long productId) {

        Long productID = productService.deleteProduct(productId);

        return ResponseEntity.ok(productID);
    }


}
