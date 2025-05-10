package com.chikacow.kohimana.service;

import com.chikacow.kohimana.dto.request.ProductRequestDTO;
import com.chikacow.kohimana.dto.response.ProductResponseDTO;

public interface ProductService {
    public ProductResponseDTO createProduct(ProductRequestDTO productRequestDTO);

    public ProductResponseDTO getProductInfo(Long id);

    public ProductResponseDTO updateProductInfo(Long id, ProductRequestDTO productRequestDTO);

    public Long deleteProduct(Long id);


}
