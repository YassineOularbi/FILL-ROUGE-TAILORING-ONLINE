package com.user_management_service.dto;

import com.user_management_service.enums.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@Builder
public class AdminDto extends UserDto implements Serializable {
    public AdminDto(String username, String password, String email, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Role role, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified) {
        super(username, password, email, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, role, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
    }

    public AdminDto() {
    }
}
