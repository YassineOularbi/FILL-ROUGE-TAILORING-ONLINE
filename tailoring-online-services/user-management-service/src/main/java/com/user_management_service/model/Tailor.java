package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "tailor")
public class Tailor extends User {

    @Column(name = "bio", nullable = false)
    private String bio;

    @Column(name = "speciality", nullable = false)
    private String specialty;

    @Column(name = "rating", nullable = false, columnDefinition = "0.00")
    private Double rating;

    @OneToMany
    @JoinColumn(name = "bank_id", nullable = false)
    private List<Bank> banks;

    public Tailor(Long id, String username, String password, String email, Role role, String firstName, String lastName, Address address, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, String bio, String specialty, Double rating, List<Bank> banks) {
        super(id, username, password, email, role, firstName, lastName, address, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.bio = bio;
        this.specialty = specialty;
        this.rating = rating;
        this.banks = banks;
        this.setRole(Role.TAILOR);
    }

    public Tailor(String bio, String specialty, Double rating, List<Bank> banks) {
        this.bio = bio;
        this.specialty = specialty;
        this.rating = rating;
        this.banks = banks;
        this.setRole(Role.TAILOR);
    }

    public Tailor(){
        this.setRole(Role.TAILOR);
    }
}