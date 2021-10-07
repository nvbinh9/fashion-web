package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class CategoryRequest {

    @NotEmpty(message = "Thiếu Slug")
    private String slug;

    @NotEmpty(message = "Thiếu tên thể loại")
    private String name;
}
