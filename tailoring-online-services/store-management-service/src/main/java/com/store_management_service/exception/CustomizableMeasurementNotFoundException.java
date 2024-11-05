package com.store_management_service.exception;

import com.store_management_service.model.CustomizableMeasurementKey;

public class CustomizableMeasurementNotFoundException extends RuntimeException {
    public CustomizableMeasurementNotFoundException(CustomizableMeasurementKey customizableMeasurementKey) {
        super(String.format("Customizable measurement not found with id: %s & %s", customizableMeasurementKey.getModelId(), customizableMeasurementKey.getMeasurementId()));
    }
}
