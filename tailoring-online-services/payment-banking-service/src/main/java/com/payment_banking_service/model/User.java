package com.payment_banking_service.model;

import com.payment_banking_service.enums.*;
import lombok.*;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private Role role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private Date dateOfBirth;
    private Date lastLogin;
    private Status status;
    private LanguagePreference languagePreference;
    private Gender gender;
    private NotificationPreference notificationPreference;
    private Boolean emailVerified;
    private Boolean phoneVerified;
    private Boolean OAuth2;
    private Boolean is2FAuth;
    private Boolean hasFingerprint;
    private Boolean hasFaceId;
    private Boolean isVerified;
}
