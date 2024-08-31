package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
public class Customer extends User {
//    private String shippingAddress;
//    private String billingInformation;

    @Column(name = "loyalty_points", nullable = false, columnDefinition = "0")
    private Integer loyaltyPoints;

    public Customer(Long id, String username, String password, String email, Role role, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, Integer loyaltyPoints) {
        super(id, username, password, email, role, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.loyaltyPoints = loyaltyPoints;
        this.setRole(Role.CUSTOMER);
    }

    public Customer(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
        this.setRole(Role.CUSTOMER);
    }

    public Customer(){
        this.setRole(Role.CUSTOMER);
    }
}
