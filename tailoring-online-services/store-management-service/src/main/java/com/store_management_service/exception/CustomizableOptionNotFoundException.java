package com.store_management_service.exception;

public class CustomizableOptionNotFoundException extends RuntimeException {
    public CustomizableOptionNotFoundException(Long id) {
        super(STR."Customizable option not found with id : \{id}");
    }
}
