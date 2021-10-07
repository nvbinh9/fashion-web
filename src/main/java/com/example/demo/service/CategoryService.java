package com.example.demo.service;

import com.example.demo.dto.request.CategoryRequest;
import com.example.demo.dto.respose.CategoryResponse;
import com.example.demo.entity.Category;

import javax.servlet.http.HttpServletRequest;

public interface CategoryService {
    CategoryResponse saveCategory(CategoryRequest categoryRequest, HttpServletRequest request);
    CategoryResponse getCategoryById(Long id);
    CategoryResponse updateCategoryById(CategoryRequest categoryRequest, HttpServletRequest request, Long id);
    String deleteCategoryById(Long id);
}
