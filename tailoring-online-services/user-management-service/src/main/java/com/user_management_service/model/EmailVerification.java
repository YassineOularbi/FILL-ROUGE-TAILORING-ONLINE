package com.user_management_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class EmailVerification {

        @Id
        @Column(nullable = false, name = "email", unique = true)
        private String email;
        @Column(nullable = false, name = "verification_code")
        private String verificationCode;
}
