package com.example.ecommerce.repository;

import com.example.ecommerce.model.ProductAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductAvailabilityRepository extends JpaRepository<ProductAvailability, Long>,
        JpaSpecificationExecutor<ProductAvailability> {
}