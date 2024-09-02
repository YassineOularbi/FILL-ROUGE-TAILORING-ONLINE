package com.user_management_service.dto;

import com.user_management_service.enums.*;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
//    private AddressDto address;
    private String phoneNumber;
    private String profilePicture;
    private Date dateOfBirth;
    private Date lastLogin;
    private Role role;
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
