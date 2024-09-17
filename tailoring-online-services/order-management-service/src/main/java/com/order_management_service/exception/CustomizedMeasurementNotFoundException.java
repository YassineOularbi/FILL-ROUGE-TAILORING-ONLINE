package com.order_management_service.exception;

public class CustomizedMeasurementNotFoundException extends RuntimeException {
    public CustomizedMeasurementNotFoundException(Long id) {
        super(STR."Customized Measurement not found with ID: \{id}");
    }
}
