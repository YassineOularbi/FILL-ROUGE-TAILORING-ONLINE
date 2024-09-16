package com.store_management_service.exception;

import com.store_management_service.model.CustomizableMeasurementKey;

public class CustomizableMeasurementExistException extends RuntimeException {
    public CustomizableMeasurementExistException(CustomizableMeasurementKey customizableMeasurementKey){
        super(STR."Customizable measurement with id : \{customizableMeasurementKey.getModelId()} & \{customizableMeasurementKey.getMeasurementId()} already existe");
    }
}
