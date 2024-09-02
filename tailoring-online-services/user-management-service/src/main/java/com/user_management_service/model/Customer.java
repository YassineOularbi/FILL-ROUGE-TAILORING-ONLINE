package com.user_management_service.model;

import com.user_management_service.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@Table(name = "customer")
public class Customer extends User implements Serializable {

    @OneToOne
    @JoinColumn(name = "shipping_address_id")
    private Address shippingAddress;

    @OneToMany(mappedBy = "customer")
    private List<Bank> banks;

    @Column(name = "loyalty_points", nullable = false, columnDefinition = "integer default 0")
    private Integer loyaltyPoints;

    public Customer(Long id, String username, String password, String email, Role role, String firstName, String lastName, Address address, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, Address shippingAddress, List<Bank> banks, Integer loyaltyPoints) {
        super(id, username, password, email, role, firstName, lastName, address, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.shippingAddress = shippingAddress;
        this.banks = banks;
        this.loyaltyPoints = loyaltyPoints;
        this.setRole(Role.CUSTOMER);
    }

    public Customer(Address shippingAddress, List<Bank> banks, Integer loyaltyPoints) {
        this.shippingAddress = shippingAddress;
        this.banks = banks;
        this.loyaltyPoints = loyaltyPoints;
        this.setRole(Role.CUSTOMER);
    }

    public Customer(){
        this.setRole(Role.CUSTOMER);
    }
}
