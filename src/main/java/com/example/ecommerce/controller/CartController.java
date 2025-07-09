package com.example.ecommerce.controller;

import com.example.ecommerce.model.CartProduct;
import com.example.ecommerce.repository.CartProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartProductRepository cartProductRepository;

    @GetMapping("/{cartId}/products")
    public List<CartProduct> getCartProducts(@PathVariable Long cartId) {
        return cartProductRepository.findByCartId(cartId);
    }
}