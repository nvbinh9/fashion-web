package com.example.demo.service;

import com.example.demo.dto.request.ProductDTO;
import com.example.demo.dto.respose.ProductResponseDTO;

public interface ProductService {

    String saveProduct(ProductDTO productDTO);
    ProductResponseDTO getProductById(Long id);
}
