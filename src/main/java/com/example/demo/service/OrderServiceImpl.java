package com.example.demo.service;

import com.example.demo.common.configuration.RabbitMQConfig;
import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.DataMailDTO;
import com.example.demo.dto.request.OrderRequest;
import com.example.demo.dto.respose.CartResponse;
import com.example.demo.dto.respose.CreateBy;
import com.example.demo.dto.respose.OrderResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.entity.User;
import com.example.demo.repository.*;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.util.Const;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private MailService mailService;



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
        orderResponse.setCreateAt(LocalDate.now());
        orderResponse.setOrderPrice(orderRequest.getPrice());

        carts.forEach(delete -> cartRepository.delete(delete));


        return orderResponse;
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Đơn hàng không tồn tại"));

        List<String> productIdString = Arrays.asList(order.getProductIds().split("-"));
        List<String> quantityIdString = Arrays.asList(order.getQuantityIds().split("-"));

        List<Long> productIds = new ArrayList<>();
        productIdString.forEach(p -> {
            productIds.add(Long.parseLong(p));
        });
        List<Long> quantityIds = new ArrayList<>();
        quantityIdString.forEach(p -> {
            quantityIds.add(Long.parseLong(p));
        });

        List<Product> products = new ArrayList<>();
        productIds.forEach(p -> {
            products.add(productRepository.findById(p)
                    .orElseThrow(() -> new NotFoundIdException("Không tìm thấy sản phẩm trong giỏ hàng. ")));
        });

        List<CartResponse> cartResponses = new ArrayList<>();
        products.forEach(cart -> {
            cartResponses.add(mapper.map(cart, CartResponse.class));
        });
        cartResponses.forEach(p -> {
            p.setQuantity(quantityIds.get(0));
            quantityIds.remove(0);
        });

        User user = userRepository.findById(order.getCreatedBy())
                .orElseThrow((() -> new NotFoundIdException("Không tìm thấy user")));
        CreateBy createBy = new CreateBy();
        createBy.setId(user.getId());
        createBy.setLastName(user.getLastName());
        createBy.setFirstName(user.getFirstName());


        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderPrice(order.getPrice());
        orderResponse.setCartResponse(cartResponses);
        orderResponse.setCreateBy(createBy);
        orderResponse.setCreateAt(order.getCreatedAt());

        return orderResponse;

    }


    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void sendMailOrder(OrderResponse orderResponse) {

        User user = userRepository.findById(orderResponse.getCreateBy().getId())
                .orElseThrow(() -> new NotFoundIdException("Không tìm thấy user"));

        try {
            DataMailDTO dataMailDTO = new DataMailDTO();
            dataMailDTO.setTo(user.getEmail());
            dataMailDTO.setSubject(Const.SEND_MAIL_SUBJECT.CLIENT_ORDER);
            Map<String, Object> props = new HashMap<>();
            props.put("name", user.getFirstName());
            props.put("price", orderResponse.getOrderPrice());
            props.put("name1", orderResponse.getCartResponse().get(0).getProductName());
            props.put("price1", orderResponse.getCartResponse().get(0).getProductPrice());
            props.put("quantity1", orderResponse.getCartResponse().get(0).getQuantity());
            props.put("name2", orderResponse.getCartResponse().get(1).getProductName());
            props.put("price2", orderResponse.getCartResponse().get(1).getProductPrice());
            props.put("quantity2", orderResponse.getCartResponse().get(1).getQuantity());

            dataMailDTO.setProps(props);
            mailService.sendHtmlMail(dataMailDTO,Const.TEMPLATE_FILE_NAME.CLIENT_ORDER );
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
