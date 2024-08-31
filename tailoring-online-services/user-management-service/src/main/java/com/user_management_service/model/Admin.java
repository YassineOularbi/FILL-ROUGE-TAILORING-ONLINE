package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.persistence.Entity;
import lombok.*;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
public class Admin extends User{
    public Admin(Long id, String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified) {
        super(id, username, password, email, role, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.setRole(Role.ADMIN);
    }

    public Admin() {
        this.setRole(Role.ADMIN);
    }
}
