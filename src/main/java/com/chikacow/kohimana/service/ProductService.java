package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.PageResponse;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;
import com.chikacow.kohimana.model.Product;

import java.util.List;

public interface ProductService {
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductInfo(Long id);

    public ProductResponseDTO updateProductInfo(Long id, ProductRequestDTO productRequestDTO);

    public Long deleteProduct(Long id);

    public Product getProductById(Long id);

    public ProductResponseDTO createTonsProduct(List<ProductRequestDTO> productRequestDTOlist);

    public PageResponse<?> getAllProducts(int pageNo, int pageSize, String sortBy);
}
