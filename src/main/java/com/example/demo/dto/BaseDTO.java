package com.example.demo.dto;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseDTO {
    private String id;
    private User createdBy;
    private User modifiedBy;
    private String createDate;
    private String modifiedDate;
}
