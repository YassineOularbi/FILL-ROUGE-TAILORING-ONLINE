package com.store_management_service.repository;

import com.store_management_service.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByTailorId(String id);
}
