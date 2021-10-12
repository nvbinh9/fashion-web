package com.example.demo.controller;

import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.respose.OrderResponse;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request)  {
        return ResponseEntity.status(201).body(orderService.createOrder(orderRequest, request));
    }

    @PostMapping("{id}")
    @PreAuthorize(" hasAdmin('ADMIN')")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.status(200).body(orderService.cancelOrder(id));

    }
}
