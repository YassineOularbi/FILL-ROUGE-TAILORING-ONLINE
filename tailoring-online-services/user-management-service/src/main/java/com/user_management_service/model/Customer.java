package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
public class Customer extends User implements Serializable {

    @Min(value = 0, message = "Loyalty points must be zero or positive")
    private Integer loyaltyPoints = 0;

    public Customer(String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, Integer loyaltyPoints) {
        super(username, password, email, role, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender);
        this.loyaltyPoints = (loyaltyPoints != null) ? loyaltyPoints : 0;
        this.setRole(Role.CUSTOMER);
    }

    public Customer(Integer loyaltyPoints) {
        this.loyaltyPoints = (loyaltyPoints != null) ? loyaltyPoints : 0;
        this.setRole(Role.CUSTOMER);
    }

    public Customer() {
        this.loyaltyPoints = 0;
        this.setRole(Role.CUSTOMER);
    }
}
