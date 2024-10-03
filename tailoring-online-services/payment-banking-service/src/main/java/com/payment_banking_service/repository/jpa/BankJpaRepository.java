package com.payment_banking_service.repository.jpa;

import com.payment_banking_service.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankJpaRepository extends JpaRepository<Bank, Long> {
    Optional<Bank> findByUserId(String id);
}
