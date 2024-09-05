package com.payment_banking_service.exception;

public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(Long id){
        super(STR."Bank not found with id: \{id}");
    }
}