package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductDTO {

    @NotEmpty(message = "Thiếu tên sản phẩm")
    private String title;

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

    @NotEmpty(message = "Chưa điền giá sản phẩm")
    private String price;
}
