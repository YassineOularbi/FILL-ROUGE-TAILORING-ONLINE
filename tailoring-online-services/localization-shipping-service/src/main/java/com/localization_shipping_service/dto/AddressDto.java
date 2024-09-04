package com.localization_shipping_service.dto;

import lombok.*;

import java.io.Serializable;

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
}
