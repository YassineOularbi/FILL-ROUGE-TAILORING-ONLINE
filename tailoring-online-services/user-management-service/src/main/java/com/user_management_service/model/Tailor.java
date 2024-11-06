package com.user_management_service.model;

import com.user_management_service.enums.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class Tailor extends User implements Serializable {

    private String bio;

    private List<Speciality> specialties;

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