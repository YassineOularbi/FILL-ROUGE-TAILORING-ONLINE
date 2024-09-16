package com.store_management_service.repository;

import com.store_management_service.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> getAllByStoreId(Long id);
}
