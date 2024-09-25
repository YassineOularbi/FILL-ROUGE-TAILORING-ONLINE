package com.order_management_service;

import com.order_management_service.controller.CustomizedMeasurementController;
import com.order_management_service.dto.CustomizedMeasurementDto;
import com.order_management_service.model.CustomizedMeasurement;
import com.order_management_service.service.CustomizedMeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomizedMeasurementControllerTests {

    @Mock
    private CustomizedMeasurementService customizedMeasurementService;

    @InjectMocks
    private CustomizedMeasurementController customizedMeasurementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizedMeasurements_Success() {
        List<CustomizedMeasurement> measurements = Arrays.asList(new CustomizedMeasurement(), new CustomizedMeasurement());
        when(customizedMeasurementService.getAllCustomizedMeasurements()).thenReturn(measurements);

        ResponseEntity<?> response = customizedMeasurementController.getAllCustomizedMeasurements();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurements, response.getBody());
    }

    @Test
    void getAllCustomizedMeasurements_Exception() {
        when(customizedMeasurementService.getAllCustomizedMeasurements()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedMeasurementController.getAllCustomizedMeasurements();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getCustomizedMeasurementById_Success() {
        Long id = 1L;
        CustomizedMeasurement measurement = new CustomizedMeasurement();
        when(customizedMeasurementService.getCustomizedMeasurementById(id)).thenReturn(measurement);

        ResponseEntity<?> response = customizedMeasurementController.getCustomizedMeasurementById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurement, response.getBody());
    }

    @Test
    void getCustomizedMeasurementById_NotFound() {
        Long id = 1L;
        when(customizedMeasurementService.getCustomizedMeasurementById(id)).thenThrow(new RuntimeException("Measurement not found"));

        ResponseEntity<?> response = customizedMeasurementController.getCustomizedMeasurementById(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Measurement not found", response.getBody());
    }

    @Test
    void getCustomizedMeasurementWithDetails_Success() {
        Long id = 1L;
        CustomizedMeasurementDto measurementDto = new CustomizedMeasurementDto();
        when(customizedMeasurementService.getCustomizedMeasurementWithDetails(id)).thenReturn(measurementDto);

        ResponseEntity<?> response = customizedMeasurementController.getCustomizedMeasurementWithDetails(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurementDto, response.getBody());
    }

    @Test
    void getCustomizedMeasurementWithDetails_NotFound() {
        Long id = 1L;
        when(customizedMeasurementService.getCustomizedMeasurementWithDetails(id)).thenThrow(new RuntimeException("Measurement not found"));

        ResponseEntity<?> response = customizedMeasurementController.getCustomizedMeasurementWithDetails(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Measurement not found", response.getBody());
    }

    @Test
    void addCustomizedMeasurement_Success() {
        Long measurementId = 1L;
        Long productId = 1L;
        CustomizedMeasurementDto measurementDto = new CustomizedMeasurementDto();
        when(customizedMeasurementService.addCustomizedMeasurement(measurementDto, productId, measurementId)).thenReturn(measurementDto);

        ResponseEntity<?> response = customizedMeasurementController.addCustomizedMeasurement(measurementDto, String.valueOf(measurementId), String.valueOf(productId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurementDto, response.getBody());
    }

    @Test
    void addCustomizedMeasurement_Exception() {
        Long measurementId = 1L;
        Long productId = 1L;
        CustomizedMeasurementDto measurementDto = new CustomizedMeasurementDto();
        when(customizedMeasurementService.addCustomizedMeasurement(measurementDto, productId, measurementId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedMeasurementController.addCustomizedMeasurement(measurementDto, String.valueOf(measurementId), String.valueOf(productId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void updateCustomizedMeasurement_Success() {
        Long id = 1L;
        CustomizedMeasurementDto measurementDto = new CustomizedMeasurementDto();
        when(customizedMeasurementService.updateCustomizedMeasurement(id, measurementDto)).thenReturn(measurementDto);

        ResponseEntity<?> response = customizedMeasurementController.updateCustomizedMeasurement(String.valueOf(id), measurementDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(measurementDto, response.getBody());
    }

    @Test
    void updateCustomizedMeasurement_NotFound() {
        Long id = 1L;
        CustomizedMeasurementDto measurementDto = new CustomizedMeasurementDto();
        when(customizedMeasurementService.updateCustomizedMeasurement(id, measurementDto)).thenThrow(new RuntimeException("Measurement not found"));

        ResponseEntity<?> response = customizedMeasurementController.updateCustomizedMeasurement(String.valueOf(id), measurementDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Measurement not found", response.getBody());
    }

    @Test
    void deleteCustomizedMeasurement_Success() {
        Long id = 1L;
        doNothing().when(customizedMeasurementService).deleteCustomizedMeasurement(id);

        ResponseEntity<?> response = customizedMeasurementController.deleteCustomizedMeasurement(String.valueOf(id));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizedMeasurement_NotFound() {
        Long id = 1L;
        doThrow(new RuntimeException("Measurement not found")).when(customizedMeasurementService).deleteCustomizedMeasurement(id);

        ResponseEntity<?> response = customizedMeasurementController.deleteCustomizedMeasurement(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Measurement not found", response.getBody());
    }
}
