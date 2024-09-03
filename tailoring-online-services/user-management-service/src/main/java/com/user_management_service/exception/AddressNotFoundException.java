package com.user_management_service.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long id) {
        super(STR."Address not found with id: \{id}");
    }
}

