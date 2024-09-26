package com.localization_shipping_service.exception;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(Long id) {
        super(String.format("Address not found with id: %s", id));
    }
}

