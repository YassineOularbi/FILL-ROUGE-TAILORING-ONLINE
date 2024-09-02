package com.user_management_service.dto;

import com.user_management_service.enums.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class CustomerDto extends UserDto implements Serializable {

//    private AddressDto shippingAddress;
    private Integer loyaltyPoints;

    public CustomerDto(String username, String password, String email, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Role role, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, Integer loyaltyPoints) {
        super(username, password, email, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, role, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
        this.loyaltyPoints = loyaltyPoints;
    }

    public CustomerDto(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public CustomerDto(){
    }
}
