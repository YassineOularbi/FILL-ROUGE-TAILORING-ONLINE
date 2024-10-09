package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tailor")
public class Tailor extends User implements Serializable {

    @Column(name = "bio", nullable = false)
    private String bio;

    @Column(name = "speciality", nullable = false)
    private String specialty;

    @Column(name = "rating", nullable = false, columnDefinition = "Decimal(10,2) default '0.00'")
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