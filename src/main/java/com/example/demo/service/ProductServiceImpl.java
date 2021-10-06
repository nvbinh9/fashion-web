package com.example.demo.service;

import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.ProductDTO;
import com.example.demo.dto.respose.CategoryResponse;
import com.example.demo.dto.respose.CreateBy;
import com.example.demo.dto.respose.ProductResponseDTO;
import com.example.demo.dto.respose.UpdateBy;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserRepository userRepository;


    @Override
    public ProductResponseDTO saveProduct(ProductDTO productDTO, HttpServletRequest request) {
        Product product = mapper.map(productDTO, Product.class);
        product.setCategory(categoryRepository.findCategoryBySlug(productDTO.getCategorySlug()));
        productRepository.save(product);

        User user = jwtAuthenticationFilter.getUser(request);

        CreateBy createdBy = new CreateBy();
        createdBy.setFirstName(user.getFirstName());
        createdBy.setLastName(user.getLastName());
        createdBy.setId(user.getId());

        UpdateBy updateBy = new UpdateBy();
        updateBy.setFirstName(user.getFirstName());
        updateBy.setLastName(user.getLastName());
        updateBy.setId(user.getId());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(product.getCategory().getId());
        categoryResponse.setName(product.getCategory().getName());
        categoryResponse.setSlug(product.getCategory().getSlug());

        ProductResponseDTO productResponseDTO = mapper.map(product, ProductResponseDTO.class);
        productResponseDTO.setCreateBy(createdBy);
        productResponseDTO.setCategory(categoryResponse);
        productResponseDTO.setUpdateBy(updateBy);
        productResponseDTO.setCreateAt(LocalDateTime.now());
        productResponseDTO.setUpdateAt(LocalDateTime.now());

        return productResponseDTO;
    }



    @Override
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Sản phẩm không tồn tại"));

        User user = userRepository.findById(product.getCreatedBy())
                .orElseThrow(() -> new NotFoundIdException("User không tồn tại"));

        CreateBy createdBy = new CreateBy();
        createdBy.setFirstName(user.getFirstName());
        createdBy.setLastName(user.getLastName());
        createdBy.setId(user.getId());

        UpdateBy updateBy = new UpdateBy();
        updateBy.setFirstName(user.getFirstName());
        updateBy.setLastName(user.getLastName());
        updateBy.setId(user.getId());

        ProductResponseDTO productResponseDTO = mapper.map(product, ProductResponseDTO.class);
        productResponseDTO.setUpdateAt(LocalDateTime.now());
        productResponseDTO.setCreateAt(LocalDateTime.now());
        productResponseDTO.setUpdateBy(updateBy);
        productResponseDTO.setCreateBy(createdBy);


        return productResponseDTO;
    }

    @Override
    public ProductResponseDTO updateProductById(Long id, ProductDTO productDTO, HttpServletRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Sản phẩm không tồn tại"));

        Category category = categoryRepository.findCategoryBySlug(productDTO.getCategorySlug());

        User user = jwtAuthenticationFilter.getUser(request);

        product.setCategory(category);
        product.setTitle(productDTO.getTitle());
        product.setContent(productDTO.getContent());
        product.setShortDescription(productDTO.getShortDescription());
        product.setThumbnail(productDTO.getThumbnail());
        product.setSlug(productDTO.getSlug());
        product.setSize(productDTO.getSize());
        product.setPrice(productDTO.getPrice());
        product.setUpdatedBy(user.getId());
        product.setUpdatedAt(LocalDateTime.now());

        productRepository.save(product);


        User userAddProduct = userRepository.findById(product.getCreatedBy())
                .orElseThrow(() -> new NotFoundIdException("User không tồn tại1"));

        CreateBy createdBy = new CreateBy();
        createdBy.setFirstName(userAddProduct.getFirstName());
        createdBy.setLastName(userAddProduct.getLastName());
        createdBy.setId(userAddProduct.getId());

        UpdateBy updateBy = new UpdateBy();
        updateBy.setFirstName(user.getFirstName());
        updateBy.setLastName(user.getLastName());
        updateBy.setId(user.getId());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setId(product.getCategory().getId());
        categoryResponse.setName(product.getCategory().getName());
        categoryResponse.setSlug(product.getCategory().getSlug());

        ProductResponseDTO productResponseDTO = mapper.map(product, ProductResponseDTO.class);
        productResponseDTO.setUpdateAt(LocalDateTime.now());
        productResponseDTO.setCreateAt(product.getCreatedAt());
        productResponseDTO.setUpdateBy(updateBy);
        productResponseDTO.setCreateBy(createdBy);
        productResponseDTO.setCategory(categoryResponse);

        return productResponseDTO;
    }
}
