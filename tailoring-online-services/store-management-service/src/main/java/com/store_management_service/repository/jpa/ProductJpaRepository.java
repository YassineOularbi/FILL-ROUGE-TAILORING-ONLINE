package com.store_management_service.repository.jpa;

import com.store_management_service.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByStoreId(Long id);
    List<Product> getAllByStoreId(Long id);
}
