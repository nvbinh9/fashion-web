package com.example.demo.service;

import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.CategoryRequest;
import com.example.demo.dto.respose.CategoryResponse;
import com.example.demo.dto.respose.CreateBy;
import com.example.demo.dto.respose.UpdateBy;
import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ModelMapper mapper;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserRepository userRepository;

    @Override
    public CategoryResponse saveCategory(CategoryRequest categoryRequest, HttpServletRequest request) {
        Category category = mapper.map(categoryRequest, Category.class);
        categoryRepository.save(category);

        User user = jwtAuthenticationFilter.getUser(request);

        CreateBy createBy = new CreateBy();
        createBy.setLastName(user.getLastName());
        createBy.setFirstName(user.getFirstName());
        createBy.setId(user.getId());

        UpdateBy updateBy = new UpdateBy();
        updateBy.setFirstName(user.getFirstName());
        updateBy.setLastName(user.getLastName());
        updateBy.setId(user.getId());

        CategoryResponse categoryResponse = mapper.map(category, CategoryResponse.class);
        categoryResponse.setUpdateAt(LocalDate.now());
        categoryResponse.setCreateAt(LocalDate.now());
        categoryResponse.setCreateBy(createBy);
        categoryResponse.setUpdateBy(updateBy);

        return categoryResponse;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Thể loại không tồn tại"));

        User userAddCategory = userRepository.findById(category.getCreatedBy())
                .orElseThrow(() -> new NotFoundIdException("User khong ton tai"));

        CreateBy createBy = new CreateBy();
        createBy.setId(userAddCategory.getId());
        createBy.setLastName(userAddCategory.getLastName());
        createBy.setFirstName(userAddCategory.getFirstName());

        User userUpdateCategory = userRepository.findById(category.getUpdatedBy())
                .orElseThrow(() -> new NotFoundIdException("User khong ton tai"));

        UpdateBy updateBy = new UpdateBy();
        updateBy.setId(userUpdateCategory.getId());
        updateBy.setFirstName(userUpdateCategory.getFirstName());
        updateBy.setLastName(userUpdateCategory.getLastName());

        CategoryResponse categoryResponse = mapper.map(category, CategoryResponse.class);
        categoryResponse.setUpdateBy(updateBy);
        categoryResponse.setCreateBy(createBy);
        categoryResponse.setCreateAt(category.getCreatedAt());
        categoryResponse.setUpdateAt(category.getCreatedAt());
        return categoryResponse;
    }

    @Override
    public CategoryResponse updateCategoryById(CategoryRequest categoryRequest, HttpServletRequest request, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Category khong ton tai"));

        User userUpdateCategory = jwtAuthenticationFilter.getUser(request);
        User userCreateCategory = userRepository.findById(category.getCreatedBy())
                .orElseThrow(() -> new NotFoundIdException("User khong ton tai"));

        category.setName(categoryRequest.getName());
        category.setSlug(categoryRequest.getSlug());
        category.setUpdatedBy(userUpdateCategory.getId());
        category.setUpdatedAt(LocalDate.now());
        categoryRepository.save(category);

        CreateBy createBy = new CreateBy();
        createBy.setLastName(userCreateCategory.getLastName());
        createBy.setFirstName(userCreateCategory.getFirstName());
        createBy.setId(userCreateCategory.getId());

        UpdateBy updateBy = new UpdateBy();
        updateBy.setLastName(userUpdateCategory.getLastName());
        updateBy.setFirstName(userUpdateCategory.getFirstName());
        updateBy.setId(userUpdateCategory.getId());

        CategoryResponse categoryResponse = mapper.map(category, CategoryResponse.class);
        categoryResponse.setUpdateAt(LocalDate.now());
        categoryResponse.setUpdateBy(updateBy);
        categoryResponse.setCreateAt(category.getCreatedAt());
        categoryResponse.setCreateBy(createBy);
        return categoryResponse;
    }

    @Override
    public String deleteCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Category khong ton tai"));
        categoryRepository.delete(category);
        return "Da xoa thanh cong";
    }


}
