package com.store_management_service.exception;

import com.store_management_service.model.CustomizableOption;
import com.store_management_service.model.Material;

public class CustomizableOptionMaterialException extends RuntimeException {
    public CustomizableOptionMaterialException(Material material, CustomizableOption option) {
        super(STR."Material with id \{material.getId()} & type \{material.getType()} doesn't match to the customizable option with id \{option.getId()} & type \{option.getType()}");
    }
}
