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
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<Long> productId = new ArrayList<>();
        orderRequest.getCartRequests().forEach(x -> {
            productId.add(Long.valueOf(x.getProductId()));
        });
//
        List<Cart> carts = new ArrayList<>();
        productId.forEach(p -> {
            carts.add(cartRepository.findCartByProductId(p)
                    .orElseThrow(() -> new NotFoundIdException("Không tìm thấy sản phẩm trong giỏ hàng.")));
        });

        List<InformationOrder> informationOrders = new ArrayList<>();
        carts.forEach(cart -> {
            informationOrders.add(mapper.map(cart, InformationOrder.class));
        });

        ArrayList<Long> quantityProduct = new ArrayList<>();
        orderRequest.getCartRequests().forEach(x -> {
            quantityProduct.add(x.getQuantity());
        });
        informationOrders.forEach(p -> {
            p.setOrder(order);
            p.setQuantity(quantityProduct.get(0));
            quantityProduct.remove(0);
        });


        User user = jwtAuthenticationFilter.getUser(request);
        CreateBy createBy = new CreateBy();
        createBy.setId(user.getId());
        createBy.setLastName(user.getLastName());
        createBy.setFirstName(user.getFirstName());

        order.setPrice(orderRequest.getPrice());
        order.setInformationOrders(informationOrders);
        orderRepository.save(order);

        List<CartResponse> cartResponses = new ArrayList<>();
        informationOrders.forEach(cart -> {
            cartResponses.add(mapper.map(cart, CartResponse.class));
        });

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setCartResponse(cartResponses);
        orderResponse.setCreateBy(createBy);
        orderResponse.setCreateAt(LocalDateTime.now());
        orderResponse.setOrderPrice(orderRequest.getPrice());

        carts.forEach(delete -> cartRepository.delete(delete));


        return orderResponse;
    }
}
