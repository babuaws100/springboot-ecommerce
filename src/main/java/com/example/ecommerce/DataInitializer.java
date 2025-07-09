package com.example.ecommerce;

import com.example.ecommerce.model.*;
import com.example.ecommerce.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final SellerRepository sellerRepo;
    private final ProductAvailabilityRepository availabilityRepo;
    private final CartRepository cartRepo;
    private final CartProductRepository cartProductRepo;
    private final OrderRepository orderRepo;

    @Value("${app.seed-data:false}")
    private boolean seedData;

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    public void init() {
        if (!seedData) return;

        try (Connection conn = dataSource.getConnection()) {
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("ecommerce_sample_data.sql"));
            System.out.println("✅ Sample data loaded successfully.");
        } catch (Exception e) {
            System.err.println("❌ Error loading sample data: " + e.getMessage());
        }
    }
}