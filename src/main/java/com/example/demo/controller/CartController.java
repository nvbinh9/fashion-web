package com.example.demo.controller;

import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.respose.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addProduct(@RequestBody  CartRequest cartRequest) {
        return ResponseEntity.status(201).body(cartService.addProduct(cartRequest));
    }

    @GetMapping
    public ResponseEntity<List<Cart>> getAllProductInCart() {
        return ResponseEntity.status(200).body(cartService.getAllProductInCart());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductInCart(@PathVariable Long id) {
        return ResponseEntity.status(200).body(cartService.deleteProductInCart(id));
    }
}
