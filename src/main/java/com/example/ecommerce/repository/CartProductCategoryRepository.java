package com.example.ecommerce.repository;

import com.example.ecommerce.model.CartProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductCategoryRepository extends JpaRepository<CartProductCategory, Long>, JpaSpecificationExecutor<CartProductCategory> {
}
