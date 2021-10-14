package com.example.demo.controller;

import com.example.demo.common.configuration.RabbitMQConfig;
import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.respose.OrderResponse;
import com.example.demo.service.OrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request)  {
        return ResponseEntity.status(201).body(orderService.createOrder(orderRequest, request));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('USER') or hasAdmin('ADMIN')")
    public ResponseEntity<String> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.status(200).body(orderService.cancelOrder(id));

    }

    @PostMapping("/publish/{id}")
    @PreAuthorize("hasRole('USER') or hasAdmin('ADMIN')")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse orderResponse = orderService.getOrderById(id);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, orderResponse);
        return ResponseEntity.status(200).body(orderResponse);
    }

}
