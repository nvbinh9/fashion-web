package com.example.demo.dto.respose;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartResponse {

    private String productName;
    private String productPrice;
    private Long quantity;
}
