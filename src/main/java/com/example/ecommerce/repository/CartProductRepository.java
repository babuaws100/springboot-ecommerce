package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long>, JpaSpecificationExecutor<CartProduct> {
    List<CartProduct> findByCartId(Long cartId);
}