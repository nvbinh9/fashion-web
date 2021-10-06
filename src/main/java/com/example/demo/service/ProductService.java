package com.example.demo.service;

import com.example.demo.dto.request.ProductDTO;
import com.example.demo.dto.respose.ProductResponseDTO;
import com.example.demo.entity.Product;

import javax.servlet.http.HttpServletRequest;

public interface ProductService {

    ProductResponseDTO saveProduct(ProductDTO productDTO, HttpServletRequest request);
    ProductResponseDTO getProductById(Long id);
    ProductResponseDTO updateProductById(Long id, ProductDTO productDTO, HttpServletRequest request);
}
