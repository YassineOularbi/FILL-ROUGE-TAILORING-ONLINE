package com.payment_banking_service.dto;

import lombok.*;

import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankDto implements Serializable {
    private String cardNumber;
    private Date expirationDate;
    private String cvc;
}
