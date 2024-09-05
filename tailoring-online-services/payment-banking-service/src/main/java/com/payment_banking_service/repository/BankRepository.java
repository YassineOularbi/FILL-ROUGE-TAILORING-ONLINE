package com.payment_banking_service.repository;

import com.payment_banking_service.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankRepository extends JpaRepository<Bank, Long> {
}
