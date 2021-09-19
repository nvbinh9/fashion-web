package com.example.demo.service;

import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.ProductDTO;
import com.example.demo.dto.respose.ProductResponseDTO;
import com.example.demo.entity.Product;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public String saveProduct(ProductDTO productDTO) {
        Product product = mapper.map(productDTO, Product.class);
        product.setCategory(categoryRepository.findCategoryEntityBySlug(productDTO.getCategorySlug()));
        productRepository.save(product);

        return "Product saved successfully";
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Sản phẩm không tồn tại"));
        ProductResponseDTO productResponseDTO = mapper.map(product, ProductResponseDTO.class);
        return productResponseDTO;
    }
}
