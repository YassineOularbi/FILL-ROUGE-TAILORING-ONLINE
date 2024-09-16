package com.store_management_service.exception;

import com.store_management_service.model.CustomizableMeasurementKey;

public class CustomizableMeasurementNotFoundException extends RuntimeException {
    public CustomizableMeasurementNotFoundException(CustomizableMeasurementKey customizableMeasurementKey) {
        super(STR."Customizable measurement not found with id : \{customizableMeasurementKey.getModelId()} & \{customizableMeasurementKey.getMeasurementId()}");
    }
}
