package com.store_management_service.exception;

public class MeasurementNotFoundException extends RuntimeException {
    public MeasurementNotFoundException(Long id) {
        super(String.format("Measurement not found with id: %s", id));
    }
}
