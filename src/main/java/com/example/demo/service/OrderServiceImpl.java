package com.example.demo.service;

import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.CartRequest;
import com.example.demo.dto.respose.CartResponse;
import com.example.demo.dto.respose.CreateBy;
import com.example.demo.dto.respose.OrderResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    public OrderResponse createOrder(List<CartRequest> cartRequests, HttpServletRequest request) {
        List<Long> productId = new ArrayList<>();
        cartRequests.forEach(x -> {
            productId.add(Long.valueOf(x.getProductId()));
        });

        List<Cart> carts = new ArrayList<>();
        productId.forEach(p -> {
            carts.add(cartRepository.findCartByProductId(p)
                    .orElseThrow(() -> new NotFoundIdException("Không tìm thấy sản phẩm trong giỏ hàng.")));
        });

        List<Long> priceOneProduct = new ArrayList<>();
        cartRequests.forEach(cartRequest -> {
            priceOneProduct.add(Long.valueOf(cartRequest.getProductId() * cartRequest.getQuantity()));
        });

        int orderPrice = 0;
        for( Long element : priceOneProduct) {
            orderPrice += element;
        }

        Order order = new Order();
        order.setOrderPrice(orderPrice);
        order.setCarts(carts);
        orderRepository.save(order);

        List<CartResponse> cartResponses = new ArrayList<>();
        carts.forEach(cart -> {
            cartResponses.add(mapper.map(cart, CartResponse.class));
        });

        User user = jwtAuthenticationFilter.getUser(request);
        CreateBy createBy = new CreateBy();
        createBy.setId(user.getId());
        createBy.setLastName(user.getLastName());
        createBy.setFirstName(user.getFirstName());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCartResponse(cartResponses);
        orderResponse.setCreateBy(createBy);
        orderResponse.setCreateAt(LocalDateTime.now());
        orderResponse.setOrderPrice(orderPrice);

        carts.forEach(delete -> cartRepository.delete(delete));


        return orderResponse;
    }
}
