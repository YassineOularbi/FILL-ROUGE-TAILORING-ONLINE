package com.store_management_service.repository.jpa;

import com.store_management_service.enums.MaterialType;
import com.store_management_service.model.CustomizableOption;
import com.store_management_service.model.ThreeDModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomizableOptionJpaRepository extends JpaRepository<CustomizableOption, Long> {
    List<CustomizableOption> getAllByModel(ThreeDModel model);
    boolean existsByModelAndType(ThreeDModel model, MaterialType materialType);
}
