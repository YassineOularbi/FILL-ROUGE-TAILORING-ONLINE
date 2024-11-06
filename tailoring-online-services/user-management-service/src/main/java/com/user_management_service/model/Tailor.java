package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class Tailor extends User implements Serializable {

    @NotNull(message = "Bio cannot be null")
    @Size(max = 250, message = "Bio cannot exceed 250 characters")
    private String bio;

    @NotNull(message = "Specialties cannot be null")
    @Size(min = 1, message = "At least one specialty is required")
    private List<Speciality> specialties;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0")
    private Double rating;

    public Tailor(String bio, List<Speciality> specialties, Double rating) {
        this.bio = bio;
        this.specialties = specialties;
        this.rating = rating;
        this.setRole(Role.TAILOR);
    }

    public Tailor(String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, String bio, List<Speciality> specialties, Double rating) {
        super(username, password, email, role, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender);
        this.bio = bio;
        this.specialties = specialties;
        this.rating = rating;
        this.setRole(Role.TAILOR);
    }

    public Tailor(){
        this.setRole(Role.TAILOR);
    }
}
