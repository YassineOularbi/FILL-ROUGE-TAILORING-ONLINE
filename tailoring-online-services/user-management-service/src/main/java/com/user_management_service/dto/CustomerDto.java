package com.user_management_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto extends UserDto {

    private AddressDto shippingAddress;
    private List<BankDto> banks;
    private Integer loyaltyPoints;
}
