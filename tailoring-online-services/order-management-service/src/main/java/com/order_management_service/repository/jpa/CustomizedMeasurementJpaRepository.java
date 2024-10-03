package com.order_management_service.repository.jpa;

import com.order_management_service.model.CustomizedMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomizedMeasurementJpaRepository extends JpaRepository<CustomizedMeasurement, Long> {
    Optional<CustomizedMeasurement> findByMeasurementIdAndProductId(Long measurementId, Long product_id);
}
