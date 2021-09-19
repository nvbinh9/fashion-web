package com.example.demo.controller;

import com.example.demo.dto.request.ProductDTO;
import com.example.demo.dto.respose.ProductResponseDTO;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductDTO productDTO) {
        return ResponseEntity.status(201).body(productService.saveProduct(productDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.status(201).body(productService.getProductById(id));
    }
}
