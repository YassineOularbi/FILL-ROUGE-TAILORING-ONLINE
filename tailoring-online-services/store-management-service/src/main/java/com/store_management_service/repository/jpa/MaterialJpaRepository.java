package com.store_management_service.repository.jpa;

import com.store_management_service.model.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialJpaRepository extends JpaRepository<Material, Long> {
    Page<Material> getAllByStoreId(Long id, Pageable pageable);
}
