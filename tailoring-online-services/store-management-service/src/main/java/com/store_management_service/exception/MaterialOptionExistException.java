package com.store_management_service.exception;

import com.store_management_service.model.MaterialOptionKey;

public class MaterialOptionExistException extends RuntimeException {
    public MaterialOptionExistException(MaterialOptionKey materialOptionKey){
        super(STR."Material option with id : \{materialOptionKey.getMaterialId()} & \{materialOptionKey.getOptionId()}, already existe");
    }
}
