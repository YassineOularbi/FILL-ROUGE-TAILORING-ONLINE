package com.user_management_service.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto implements Serializable {

    private String address;
    private String suite;
    private String city;
    private String province;
    private String country;
    private String zipCode;
    private Boolean isDefault;
//    private List<BankDto> banks;
//    private UserDto user;
}
