package com.example.demo.service;

import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.respose.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest, HttpServletRequest request);
    String cancelOrder(Long id);
    OrderResponse getOrderById(Long id);
}
