package com.store_management_service.repository.jpa;

import com.store_management_service.model.ThreeDModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThreeDModelJpaRepository extends JpaRepository<ThreeDModel, Long> {
    Optional<ThreeDModel> getByProductId(Long id);
}
