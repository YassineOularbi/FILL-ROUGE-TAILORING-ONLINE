package com.payment_banking_service.exception;

public class BankNotFoundException extends RuntimeException {
    public BankNotFoundException(Long id){
        super(String.format("Bank not found with id: %s", id));
    }
}
