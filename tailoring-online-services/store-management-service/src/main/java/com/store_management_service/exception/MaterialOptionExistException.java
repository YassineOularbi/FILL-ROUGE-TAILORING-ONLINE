package com.store_management_service.exception;

import com.store_management_service.model.MaterialOptionKey;

public class MaterialOptionExistException extends RuntimeException {
    public MaterialOptionExistException(MaterialOptionKey materialOptionKey){
        super(String.format("Material option with id: %s & %s already exists", materialOptionKey.getMaterialId(), materialOptionKey.getOptionId()));
    }
}
