package com.localization_shipping_service.repository;

import com.localization_shipping_service.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    Optional<Address> findByUserId(String id);
}
