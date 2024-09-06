package com.product_management_service.repository;

import com.product_management_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByStoreId(Long id);
}
