package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ResponseEntity<?> getAllProducts(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                   @Min(5) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                   @RequestParam String sortBy) {

        PageResponse<?> prods = productService.getAllProducts(pageNo, pageSize, sortBy);
        return ResponseEntity.ok(prods);

    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProductInfo(@PathVariable Long productId) {

        ProductResponseDTO res = productService.getProductInfo(productId);

        return ResponseEntity.ok(res);
    }


}
