package com.example.demo.service;

import com.example.demo.common.exception.NotFoundIdException;
import com.example.demo.dto.request.CartRequest;
import com.example.demo.entity.Cart;
import com.example.demo.entity.Product;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public Cart addProduct(CartRequest cartRequest) {
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new NotFoundIdException("Sản phẩm không tồn tại"));

        Cart cart = new Cart();
        cart.setProductId(product.getId());
        cart.setProductName(product.getProductName());
        cart.setProductPrice(product.getProductPrice());
        cartRepository.save(cart);

        return cart;
    }

    @Override
    public List<Cart> getAllProductInCart() {
        List<Cart> carts = cartRepository.findAll();
        return carts;
    }

    @Override
    public String deleteProductInCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundIdException("Sản phẩm không tồn tại"));
        cartRepository.delete(cart);
        return "Đã xóa thành công";
    }
}
