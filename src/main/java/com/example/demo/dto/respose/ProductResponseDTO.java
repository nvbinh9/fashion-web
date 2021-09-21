package com.example.demo.dto.respose;

import com.example.demo.dto.BaseDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.audit.UserDateAudit;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO extends UserDateAudit {
    private String title;
    private String content;
    private String shortDescription;
    private String thumbnail;
    private String slug;
    private Category category;
    private String size;
    private String price;
}
