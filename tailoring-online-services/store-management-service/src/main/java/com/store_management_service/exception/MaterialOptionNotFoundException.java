package com.store_management_service.exception;

import com.store_management_service.model.MaterialOptionKey;

public class MaterialOptionNotFoundException extends RuntimeException {
    public MaterialOptionNotFoundException(MaterialOptionKey materialOptionKey) {
        super(STR."Material option not found with id : \{materialOptionKey.getMaterialId()} & \{materialOptionKey.getOptionId()}");
    }
}
