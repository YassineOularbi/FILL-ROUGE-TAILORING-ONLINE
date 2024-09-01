package com.user_management_service.dto;

import com.user_management_service.enums.Gender;
import com.user_management_service.enums.LanguagePreference;
import com.user_management_service.enums.NotificationPreference;
import com.user_management_service.enums.Status;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@Builder
public class AdminDto extends UserDto{
    public AdminDto(String username, String email, String firstName, String lastName, AddressDto address, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified) {
        super(username, email, firstName, lastName, address, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
    }

    public AdminDto() {
    }
}
