package com.example.demo.controller;

import com.example.demo.dto.request.CategoryRequest;
import com.example.demo.dto.respose.CategoryResponse;
import com.example.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
     private CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> saveCategory(@RequestBody @Valid CategoryRequest categoryRequest, HttpServletRequest request) {
        return ResponseEntity.status(201).body(categoryService.saveCategory(categoryRequest, request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(categoryService.getCategoryById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategoryById(@PathVariable Long id, @RequestBody @Valid CategoryRequest categoryRequest, HttpServletRequest request) {
        return ResponseEntity.status(201).body(categoryService.updateCategoryById(categoryRequest, request, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
        return ResponseEntity.status(200).body(categoryService.deleteCategoryById(id));
    }

}
