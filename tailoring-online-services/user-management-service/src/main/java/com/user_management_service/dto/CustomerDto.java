package com.user_management_service.dto;

import com.user_management_service.enums.Gender;
import com.user_management_service.enums.LanguagePreference;
import com.user_management_service.enums.NotificationPreference;
import com.user_management_service.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CustomerDto extends UserDto implements Serializable {

//    private AddressDto shippingAddress;
//    private List<BankDto> banks;
    private Integer loyaltyPoints;

//    public CustomerDto(String username, String password, String email, String firstName, String lastName, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, Integer loyaltyPoints) {
//        super(username, password, email, firstName, lastName, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
//        this.loyaltyPoints = loyaltyPoints;
//    }
//
//    public CustomerDto(Integer loyaltyPoints) {
//        this.loyaltyPoints = loyaltyPoints;
//    }

    //    public CustomerDto(String username, String password, String email, String firstName, String lastName, AddressDto address, String phoneNumber, String profilePicture, Date dateOfBirth, Date lastLogin, Status status, LanguagePreference languagePreference, Gender gender, NotificationPreference notificationPreference, Boolean emailVerified, Boolean phoneVerified, Boolean OAuth2, Boolean is2FAuth, Boolean hasFingerprint, Boolean hasFaceId, Boolean isVerified, AddressDto shippingAddress, List<BankDto> banks, Integer loyaltyPoints) {
//        super(username, password, email, firstName, lastName, address, phoneNumber, profilePicture, dateOfBirth, lastLogin, status, languagePreference, gender, notificationPreference, emailVerified, phoneVerified, OAuth2, is2FAuth, hasFingerprint, hasFaceId, isVerified);
////        this.shippingAddress = shippingAddress;
////        this.banks = banks;
//        this.loyaltyPoints = loyaltyPoints;
//    }
//
//    public CustomerDto(AddressDto shippingAddress, List<BankDto> banks, Integer loyaltyPoints) {
////        this.shippingAddress = shippingAddress;
////        this.banks = banks;
//        this.loyaltyPoints = loyaltyPoints;
//    }

    public CustomerDto(){
    }
}
