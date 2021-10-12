package com.example.demo.service;

import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.respose.CartResponse;
import com.example.demo.dto.respose.CreateBy;
import com.example.demo.dto.respose.OrderResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.InformationOrder;
import com.example.demo.entity.Order;
import com.example.demo.entity.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.InformationOrderRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.security.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private InformationOrderRepository informationOrderRepository;


    @Override
    public OrderResponse createOrder(OrderRequest orderRequest, HttpServletRequest request) {
        Order order = new Order();
        order.setPrice(orderRequest.getPrice());
        order.setProductIds(orderRequest.getProductIds());
        order.setQuantityIds(orderRequest.getQuantityIds());
        order.setActive(true);
        orderRepository.save(order);

        List<String> productIdString = Arrays.asList(orderRequest.getProductIds().split("-"));
        List<String> quantityIdString = Arrays.asList(orderRequest.getQuantityIds().split("-"));

        List<Long> productIds = new ArrayList<>();
        productIdString.forEach(p -> {
            productIds.add(Long.parseLong(p));
        });
        List<Long> quantityIds = new ArrayList<>();
        quantityIdString.forEach(p -> {
            quantityIds.add(Long.parseLong(p));
        });

        List<Cart> carts = new ArrayList<>();
        productIds.forEach(p -> {
            carts.add(cartRepository.findCartByProductId(p)
                    .orElseThrow(() -> new NotFoundIdException("Không tìm thấy sản phẩm trong giỏ hàng. ")));
        });

        List<CartResponse> cartResponses = new ArrayList<>();
        carts.forEach(cart -> {
            cartResponses.add(mapper.map(cart, CartResponse.class));
        });
        cartResponses.forEach(p -> {
            p.setQuantity(quantityIds.get(0));
            quantityIds.remove(0);
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
        orderResponse.setOrderPrice(orderRequest.getPrice());

        carts.forEach(delete -> cartRepository.delete(delete));


        return orderResponse;
    }

    @Override
    public String cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Không tìm thấy đơn hàng"));
        order.setActive(false);
        orderRepository.save(order);
        return "Đã hủy đơn hàng";
    }
}
