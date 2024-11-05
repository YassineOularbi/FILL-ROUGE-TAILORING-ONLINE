package com.notification_mailing_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "email_verification")
public class EmailVerification implements Serializable {

        @Id
        @Column(nullable = false, name = "email", unique = true)
        private String email;
        @Column(nullable = false, name = "verification_code")
        private String verificationCode;
}
