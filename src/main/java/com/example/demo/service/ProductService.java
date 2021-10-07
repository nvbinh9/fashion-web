package com.example.demo.service;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.respose.ProductResponseDTO;

import javax.servlet.http.HttpServletRequest;

public interface ProductService {

    ProductResponseDTO saveProduct(ProductRequest productDTO, HttpServletRequest request);
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO updateProductById(Long id, ProductRequest productDTO, HttpServletRequest request);
    String deleteProductById (Long id);
}
