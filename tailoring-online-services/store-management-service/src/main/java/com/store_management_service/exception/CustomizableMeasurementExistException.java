package com.store_management_service.exception;

import com.store_management_service.model.CustomizableMeasurementKey;

public class CustomizableMeasurementExistException extends RuntimeException {
    public CustomizableMeasurementExistException(CustomizableMeasurementKey customizableMeasurementKey){
        super(String.format("Customizable measurement with id: %s & %s already exists", customizableMeasurementKey.getModelId(), customizableMeasurementKey.getMeasurementId()));
    }
}
