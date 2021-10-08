package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductRequest {

    @NotEmpty(message = "Thiếu tên sản phẩm")
    private String name;

    @NotEmpty(message = "Chưa có nội dung sản phẩm")
    private String content;

    @NotEmpty(message = "Mô tả không thể thiếu")
    private String shortDescription;

    @NotEmpty(message = "Thiếu ảnh")
    private String thumbnail;

    @NotEmpty(message = "Thiếu Slug")
    private String slug;

    @NotEmpty(message = "Danh mục không thể bỏ trống")
    private String categorySlug;

    @NotEmpty(message = "Thiếu Size")
    private String size;

    private int price;
}
