package com.store_management_service.repository;

import com.store_management_service.model.ThreeDModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreeDModelRepository extends JpaRepository<ThreeDModel, Long> {
    Optional<ThreeDModel> getByProductId(Long id);
}
