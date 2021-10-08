package com.example.demo.dto.respose;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ProductResponseDTO  {
    private int id;
    private String name;
    private String content;
    private String shortDescription;
    private String thumbnail;
    private String slug;
    private CategoryId category;
    private String size;
    private int price;

    @JsonProperty("create_by")
    private CreateBy createBy;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("update_at")
    private LocalDateTime updateAt;

    @JsonProperty("update_by")
    private UpdateBy updateBy;

}
