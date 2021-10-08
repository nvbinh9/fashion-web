package com.example.demo.service;

import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.respose.CartResponse;
import com.example.demo.entity.Cart;

import javax.validation.Valid;
import java.util.List;

public interface CartService {
    Cart addProduct(CartRequest cartRequest);
    List<Cart> getAllProductInCart();
    String deleteProductInCart(Long id);
}
