package com.store_management_service.exception;

import com.store_management_service.model.CustomizableOption;
import com.store_management_service.model.Material;

public class CustomizableOptionMaterialException extends RuntimeException {
    public CustomizableOptionMaterialException(Material material, CustomizableOption option) {
        super(String.format("Material with id %s & type %s doesn't match to the customizable option with id %s & type %s", material.getId(), material.getType(), option.getId(), option.getType()));
    }
}
