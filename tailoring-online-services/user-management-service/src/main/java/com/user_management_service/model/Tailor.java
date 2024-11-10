package com.user_management_service.model;

import com.user_management_service.enums.*;
import com.user_management_service.validation.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.time.*;
import java.util.List;

@Getter
@Setter
@ToString
public class Tailor extends User implements Serializable {

    @NotNull(message = "Bio cannot be null", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(max = 250, message = "Bio cannot exceed 250 characters", groups = {CreateGroup.class, UpdateGroup.class})
    private String bio;

    @NotNull(message = "Specialties cannot be null", groups = {CreateGroup.class, UpdateGroup.class})
    @Size(min = 1, max = 3, message = "The number of specialties must be between 1 and 3", groups = {CreateGroup.class, UpdateGroup.class})
    private List<Speciality> specialities;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be at least 0.0", groups = {CreateGroup.class, UpdateGroup.class})
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating cannot exceed 5.0", groups = {CreateGroup.class, UpdateGroup.class})
    private Double rating = 0.0;

    public Tailor(String bio, List<Speciality> specialities, Double rating) {
        this.bio = bio;
        this.specialities = specialities;
        this.rating = rating;
        this.setRole(Role.TAILOR);
    }

    public Tailor(String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, LocalDate dateOfBirth, LocalDateTime lastLogin, Status status, LanguagePreference languagePreference, Gender gender, String bio, List<Speciality> specialities, Double rating) {
        super(username, password, email, role, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender);
        this.bio = bio;
        this.specialities = specialities;
        this.rating = rating;
        this.setRole(Role.TAILOR);
    }

    public Tailor() {
        this.setRole(Role.TAILOR);
        this.rating = 0.0;
    }
}
