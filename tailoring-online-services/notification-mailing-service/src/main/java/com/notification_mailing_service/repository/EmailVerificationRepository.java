package com.notification_mailing_service.repository;

import com.notification_mailing_service.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String> {
    Optional<EmailVerification> findByEmail(String email);
}
