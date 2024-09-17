package com.order_management_service.repository;

import com.order_management_service.model.CustomizedOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomizedOptionRepository extends JpaRepository<CustomizedOption, Long> {

    Optional<CustomizedOption> findByMaterialIdAndProductId(Long materialId, Long product_id);
}
