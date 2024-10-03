package com.store_management_service.repository.jpa;

import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableMeasurementKey;
import com.store_management_service.model.ThreeDModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomizableMeasurementJpaRepository extends JpaRepository<CustomizableMeasurement, CustomizableMeasurementKey> {
    Page<CustomizableMeasurement> findAllByModel(ThreeDModel model, Pageable pageable);
}
