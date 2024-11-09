package com.user_management_service.model;

import com.user_management_service.enums.*;
import com.user_management_service.validation.CreateGroup;
import com.user_management_service.validation.UpdateGroup;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.io.Serializable;
import java.time.*;

@Getter
@Setter
public class Customer extends User implements Serializable {

    @Min(value = 0, message = "Loyalty points must be zero or positive", groups = {CreateGroup.class, UpdateGroup.class})
    private Integer loyaltyPoints = 0;

    public Customer(String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, LocalDate dateOfBirth, LocalDateTime lastLogin, Status status, LanguagePreference languagePreference, Gender gender, Integer loyaltyPoints) {
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
