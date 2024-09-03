package com.user_management_service.repository;

import com.user_management_service.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
    EmailVerification findByEmail(String email);
}
