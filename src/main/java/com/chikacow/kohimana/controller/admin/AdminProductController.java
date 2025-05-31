package com.chikacow.kohimana.controller.admin;

import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.service.CategoryService;
import com.chikacow.kohimana.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseData<?> createNewProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO res = productService.createProduct(productRequestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

//    @PostMapping("/create")
//    public ResponseEntity<ProductResponseDTO> createNewProduct(@RequestBody List<ProductRequestDTO> productRequestDTO) {
//        ProductResponseDTO res = productService.createTonsProduct(productRequestDTO);
//
//        return ResponseEntity.ok(res);
//    }



    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping("/{productId}")
    public ResponseData<?> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequestDTO productRequestDTO) {

        ProductResponseDTO res = productService.updateProductInfo(productId, productRequestDTO);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{productId}")
    public ResponseData<?> changeStatus(@PathVariable Long productId) {

        String isR = productService.changeStatus(productId);
        Map<String, String> res = new HashMap<>();
        res.put("status", isR);
        res.put("prod_id", String.valueOf(productId));


        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }


}
