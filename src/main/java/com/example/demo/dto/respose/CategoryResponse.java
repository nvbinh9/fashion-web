package com.example.demo.dto.respose;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CategoryResponse {

    private int id;
    private String slug;
    private String name;

    @JsonProperty("create_by")
    private CreateBy createBy;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("update_at")
    private LocalDateTime updateAt;

    @JsonProperty("update_by")
    private UpdateBy updateBy;
}
