package com.store_management_service.exception;

import com.store_management_service.model.MaterialOptionKey;

public class MaterialOptionNotFoundException extends RuntimeException {
    public MaterialOptionNotFoundException(MaterialOptionKey materialOptionKey) {
        super(String.format("Material option not found with id: %s & %s", materialOptionKey.getMaterialId(), materialOptionKey.getOptionId()));
    }
}
