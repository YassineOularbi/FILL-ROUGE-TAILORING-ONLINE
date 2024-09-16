package com.store_management_service.repository;

import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableMeasurementKey;
import com.store_management_service.model.ThreeDModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomizableMeasurementRepository extends JpaRepository<CustomizableMeasurement, CustomizableMeasurementKey> {
    List<CustomizableMeasurement> findAllByModel(ThreeDModel model);
}
