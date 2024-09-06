package com.store_management_service.model;

import com.store_management_service.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
public class Tailor extends User implements Serializable {
    private String bio;
    private String specialty;
    private Double rating;

    public Tailor(String id, String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, String bio, String specialty, Double rating) {
        super(id, username, password, email, role, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.bio = bio;
        this.specialty = specialty;
        this.rating = rating;
        this.setRole(Role.TAILOR);
    }

    public Tailor(String bio, String specialty, Double rating) {
        this.bio = bio;
        this.specialty = specialty;
        this.rating = rating;
        this.setRole(Role.TAILOR);
    }

    public Tailor(){
        this.setRole(Role.TAILOR);
    }
}