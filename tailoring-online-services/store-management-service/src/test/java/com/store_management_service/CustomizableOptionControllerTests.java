package com.store_management_service;

import com.store_management_service.controller.CustomizableOptionController;
import com.store_management_service.dto.CustomizableOptionDto;
import com.store_management_service.enums.MaterialType;
import com.store_management_service.exception.CustomizableOptionNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.model.CustomizableOption;
import com.store_management_service.service.CustomizableOptionService;
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

class CustomizableOptionControllerTests {

    @Mock
    private CustomizableOptionService customizableOptionService;

    @InjectMocks
    private CustomizableOptionController customizableOptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizableOptions_Success() {
        List<CustomizableOptionDto> dtos = Arrays.asList(new CustomizableOptionDto(), new CustomizableOptionDto());
        when(customizableOptionService.getAllCustomizableOptions()).thenReturn(dtos);

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());
    }

    @Test
    void getAllCustomizableOptions_Exception() {
        when(customizableOptionService.getAllCustomizableOptions()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptions();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAllCustomizableOptionsByThreeDModel_Success() {
        Long modelId = 1L;
        List<CustomizableOption> customizableOptions = Arrays.asList(new CustomizableOption(), new CustomizableOption());
        when(customizableOptionService.getAllCustomizableOptionsByThreeDModel(modelId)).thenReturn(customizableOptions);

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptionsByThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customizableOptions, response.getBody());
    }

    @Test
    void getAllCustomizableOptionsByThreeDModel_NotFound() {
        Long modelId = 1L;
        when(customizableOptionService.getAllCustomizableOptionsByThreeDModel(modelId)).thenThrow(new ThreeDModelNotFoundException(modelId));

        ResponseEntity<?> response = customizableOptionController.getAllCustomizableOptionsByThreeDModel(String.valueOf(modelId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ThreeDModelNotFoundException(modelId).getMessage(), response.getBody());
    }

    @Test
    void getCustomizableOptionById_Success() {
        Long id = 1L;
        CustomizableOption dto = new CustomizableOption();
        when(customizableOptionService.getCustomizableOptionById(id)).thenReturn(dto);

        ResponseEntity<?> response = customizableOptionController.getCustomizableOptionById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void getCustomizableOptionById_NotFound() {
        Long id = 1L;
        when(customizableOptionService.getCustomizableOptionById(id)).thenThrow(new CustomizableOptionNotFoundException(id));

        ResponseEntity<?> response = customizableOptionController.getCustomizableOptionById(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableOptionNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void addCustomizableOption_Success() {
        Long modelId = 1L;
        MaterialType type = MaterialType.ACCESSORY;
        CustomizableOptionDto dto = new CustomizableOptionDto();
        when(customizableOptionService.addCustomizableOption(modelId, type)).thenReturn(dto);

        ResponseEntity<?> response = customizableOptionController.addCustomizableOption(String.valueOf(modelId), type);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, response.getBody());
    }

    @Test
    void addCustomizableOption_Exception() {
        Long modelId = 1L;
        MaterialType type = MaterialType.ACCESSORY;
        when(customizableOptionService.addCustomizableOption(modelId, type)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizableOptionController.addCustomizableOption(String.valueOf(modelId), type);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void deleteCustomizableOption_Success() {
        Long id = 1L;
        doNothing().when(customizableOptionService).deleteCustomizableOption(id);

        ResponseEntity<?> response = customizableOptionController.deleteCustomizableOption(String.valueOf(id));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizableOption_NotFound() {
        Long id = 1L;
        doThrow(new CustomizableOptionNotFoundException(id)).when(customizableOptionService).deleteCustomizableOption(id);

        ResponseEntity<?> response = customizableOptionController.deleteCustomizableOption(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new CustomizableOptionNotFoundException(id).getMessage(), response.getBody());
    }
}
