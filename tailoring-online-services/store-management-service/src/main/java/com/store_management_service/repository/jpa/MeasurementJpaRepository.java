package com.store_management_service.repository.jpa;

import com.store_management_service.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementJpaRepository extends JpaRepository<Measurement, Long> {
}
