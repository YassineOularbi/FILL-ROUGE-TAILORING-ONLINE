package com.store_management_service.exception;

import com.store_management_service.enums.MaterialType;

public class CustomizableOptionExistException extends RuntimeException {
    public CustomizableOptionExistException(Long id, MaterialType type) {
        super(STR."Customizable option with type : \{type} already exist in Three D Model wih id : \{id}");
    }
}
