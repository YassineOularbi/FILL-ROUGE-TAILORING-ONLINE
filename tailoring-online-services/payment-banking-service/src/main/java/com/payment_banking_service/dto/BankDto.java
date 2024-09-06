package com.payment_banking_service.dto;

import com.payment_banking_service.model.User;
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
    private User user;
}
