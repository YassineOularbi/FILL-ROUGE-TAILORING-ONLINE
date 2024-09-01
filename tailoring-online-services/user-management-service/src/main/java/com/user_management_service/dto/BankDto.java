package com.user_management_service.dto;

import lombok.*;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDto {

    private Long id;
    private String cardNumber;
    private Date expirationDate;
    private String cvc;
    private AddressDto billingAddress;
    private TailorDto tailor;
    private CustomerDto customer;
}
