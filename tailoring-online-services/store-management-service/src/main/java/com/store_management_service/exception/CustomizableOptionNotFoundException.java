package com.store_management_service.exception;

public class CustomizableOptionNotFoundException extends RuntimeException {
    public CustomizableOptionNotFoundException(Long id) {
        super(String.format("Customizable option not found with id: %s", id));
    }
}
