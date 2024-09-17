package com.order_management_service.repository;

import com.order_management_service.model.CustomizedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomizedProductRepository extends JpaRepository<CustomizedProduct, Long> {
}
