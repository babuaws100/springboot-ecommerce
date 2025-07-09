package com.example.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.Immutable;

import java.beans.Transient;

@Entity
@Immutable // Optional, marks it read-only
@Subselect("""
            SELECT 
                ROW_NUMBER() OVER () AS id,
                c.id AS cart_id,
                p.id AS product_id,
                p.sku_id AS sku_id,
                p.name AS product_name,
                cat.id AS category_id,
                cat.name AS category_name
            FROM cart c
            JOIN cart_product cp ON cp.cart_id = c.id
            JOIN product p ON cp.product_id = p.id
            JOIN category cat ON p.category_id = cat.id
        """)
@Getter
@Setter
public class CartProductCategory {

    @Id
    private String id;

    @Transient
    private void generateId() {
        this.id = cartId + "-" + productId + "-" + categoryId;
    }

    private Long cartId;

    private Long productId;
    private String skuId;
    private String productName;

    private Long categoryId;
    private String categoryName;

}

