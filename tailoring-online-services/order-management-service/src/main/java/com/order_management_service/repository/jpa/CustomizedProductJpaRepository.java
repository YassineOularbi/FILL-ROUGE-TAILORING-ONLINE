package com.order_management_service.repository.jpa;

import com.order_management_service.model.CustomizedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomizedProductJpaRepository extends JpaRepository<CustomizedProduct, Long> {
}
