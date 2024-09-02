package com.user_management_service.dto;

import com.user_management_service.enums.Gender;
import com.user_management_service.enums.LanguagePreference;
import com.user_management_service.enums.NotificationPreference;
import com.user_management_service.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminDto implements Serializable {
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
