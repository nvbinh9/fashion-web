package com.example.demo.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    @JsonProperty("cartRequests")
    private List<CartRequest> cartRequests;
    private String price;
}
