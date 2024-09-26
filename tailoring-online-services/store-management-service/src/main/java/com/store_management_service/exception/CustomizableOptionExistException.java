package com.store_management_service.exception;

import com.store_management_service.enums.MaterialType;

public class CustomizableOptionExistException extends RuntimeException {
    public CustomizableOptionExistException(Long id, MaterialType type) {
        super(String.format("Customizable option with type: %s already exists in Three D Model with id: %s", type, id));
    }
}
