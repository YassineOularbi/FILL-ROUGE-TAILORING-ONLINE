package com.store_management_service;

import com.store_management_service.controller.CustomizableMeasurementController;
import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.exception.CustomizableMeasurementNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableMeasurementKey;
import com.store_management_service.service.CustomizableMeasurementService;
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

public class CustomizableMeasurementControllerTests {

    @Mock
    private CustomizableMeasurementService customizableMeasurementService;

    @InjectMocks
    private CustomizableMeasurementController customizableMeasurementController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizableMeasurement_Success() {
        List<CustomizableMeasurementDto> dtos = Arrays.asList(new CustomizableMeasurementDto(), new CustomizableMeasurementDto());
        when(customizableMeasurementService.getAllCustomizableMeasurement()).thenReturn(dtos);

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurement();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());
    }

    @Test
    void getAllCustomizableMeasurement_Exception() {
        when(customizableMeasurementService.getAllCustomizableMeasurement()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurement();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllCustomizableMeasurementByThreeDModel_Success() {
        Long modelId = 1L;
        List<CustomizableMeasurement> dtos = Arrays.asList(new CustomizableMeasurement(), new CustomizableMeasurement());
        when(customizableMeasurementService.getAllCustomizableMeasurementByThreeDModel(modelId)).thenReturn(dtos);

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurementByThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());
    }

    @Test
    void getAllCustomizableMeasurementByThreeDModel_NotFound() {
        Long modelId = 1L;
        when(customizableMeasurementService.getAllCustomizableMeasurementByThreeDModel(modelId)).thenThrow(new ThreeDModelNotFoundException(modelId));

        ResponseEntity<?> response = customizableMeasurementController.getAllCustomizableMeasurementByThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThreeDModelNotFoundException(modelId).getMessage(), response.getBody());
    }

    @Test
    void getCustomizableMeasurementById_Success() {
        Long modelId = 1L;
        Long measurementId = 1L;
        CustomizableMeasurement customizableMeasurement = new CustomizableMeasurement();
        when(customizableMeasurementService.getCustomizableMeasurementById(modelId, measurementId)).thenReturn(customizableMeasurement);

        ResponseEntity<?> response = customizableMeasurementController.getCustomizableMeasurementById(String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customizableMeasurement, response.getBody());
    }

    @Test
    void getCustomizableMeasurementById_NotFound() {
        Long modelId = 1L;
        Long measurementId = 1L;
        when(customizableMeasurementService.getCustomizableMeasurementById(modelId, measurementId)).thenThrow(new CustomizableMeasurementNotFoundException(new CustomizableMeasurementKey(modelId, measurementId)));

        ResponseEntity<?> response = customizableMeasurementController.getCustomizableMeasurementById(String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableMeasurementNotFoundException(new CustomizableMeasurementKey(modelId, measurementId)).getMessage(), response.getBody());
    }

    @Test
    void addCustomizableMeasurement_Success() {
        Long modelId = 1L;
        Long measurementId = 1L;
        CustomizableMeasurementDto dto = new CustomizableMeasurementDto();
        when(customizableMeasurementService.addCustomizableMeasurement(modelId, measurementId)).thenReturn(dto);

        ResponseEntity<?> response = customizableMeasurementController.addCustomizableMeasurement(String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void addCustomizableMeasurement_Exception() {
        Long modelId = 1L;
        Long measurementId = 1L;
        when(customizableMeasurementService.addCustomizableMeasurement(modelId, measurementId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableMeasurementController.addCustomizableMeasurement(String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void deleteCustomizableMeasurement_Success() {
        Long modelId = 1L;
        Long measurementId = 1L;
        doNothing().when(customizableMeasurementService).deleteCustomizableMeasurement(modelId, measurementId);

        ResponseEntity<?> response = customizableMeasurementController.deleteCustomizableMeasurement(String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizableMeasurement_NotFound() {
        Long modelId = 1L;
        Long measurementId = 1L;
        doThrow(new CustomizableMeasurementNotFoundException(new CustomizableMeasurementKey(modelId, measurementId))).when(customizableMeasurementService).deleteCustomizableMeasurement(modelId, measurementId);

        ResponseEntity<?> response = customizableMeasurementController.deleteCustomizableMeasurement(String.valueOf(modelId), String.valueOf(measurementId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableMeasurementNotFoundException(new CustomizableMeasurementKey(modelId, measurementId)).getMessage(), response.getBody());
    }
}
