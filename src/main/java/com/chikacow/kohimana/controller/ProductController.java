package com.chikacow.kohimana.controller;

import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.dto.response.ResponseData;
import com.chikacow.kohimana.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/list")
    public ResponseData<?> getAllProducts(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                   @Min(3) @RequestParam(defaultValue = "20", required = false) int pageSize,
                                                                   @RequestParam String sortBy) {

        PageResponse<?> res = productService.getAllProducts(pageNo, pageSize, sortBy);
        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();

    }

    @GetMapping("/{id}")
    public ResponseData<?> getProductInfo(@PathVariable Long id) {

        ProductResponseDTO res = productService.getProductInfo(id);

        return ResponseData.builder()
                .status(HttpStatus.OK.value())
                .message("Success")
                .data(res)
                .build();
    }


}
