package com.example.demo.dto.respose;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderResponse {

    @JsonProperty("product")
    private List<CartResponse> cartResponse;

    @JsonProperty("create_by")
    private CreateBy createBy;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    private String orderPrice;


}
