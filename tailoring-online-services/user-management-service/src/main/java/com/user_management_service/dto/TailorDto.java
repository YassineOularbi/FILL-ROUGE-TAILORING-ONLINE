package com.user_management_service.dto;

import com.user_management_service.enums.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@Builder
public class TailorDto extends UserDto implements Serializable {

    private String bio;
    private String specialty;
    private Double rating;

    public TailorDto(String username, String password, String email, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, String bio, String specialty, Double rating) {
        super(username, password, email, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.bio = bio;
        this.specialty = specialty;
        this.rating = rating;
    }

    public TailorDto(String bio, String specialty, Double rating) {
        this.bio = bio;
        this.specialty = specialty;
        this.rating = rating;
    }

    public TailorDto(){
    }
}
