package com.order_management_service;

import com.order_management_service.controller.CustomizedOptionController;
import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.model.CustomizedOption;
import com.order_management_service.service.CustomizedOptionService;
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

public class CustomizedOptionControllerTests {

    @Mock
    private CustomizedOptionService customizedOptionService;

    @InjectMocks
    private CustomizedOptionController customizedOptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizedOptions_Success() {
        List<CustomizedOption> options = Arrays.asList(new CustomizedOption(), new CustomizedOption());
        when(customizedOptionService.getAllCustomizedOptions()).thenReturn(options);

        ResponseEntity<?> response = customizedOptionController.getAllCustomizedOptions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(options, response.getBody());
    }

    @Test
    void getAllCustomizedOptions_Exception() {
        when(customizedOptionService.getAllCustomizedOptions()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedOptionController.getAllCustomizedOptions();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getCustomizedOptionById_Success() {
        Long id = 1L;
        CustomizedOption option = new CustomizedOption();
        when(customizedOptionService.getCustomizedOptionById(id)).thenReturn(option);

        ResponseEntity<?> response = customizedOptionController.getCustomizedOptionById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(option, response.getBody());
    }

    @Test
    void getCustomizedOptionById_NotFound() {
        Long id = 1L;
        when(customizedOptionService.getCustomizedOptionById(id)).thenThrow(new RuntimeException("Option not found"));

        ResponseEntity<?> response = customizedOptionController.getCustomizedOptionById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Option not found", response.getBody());
    }

    @Test
    void getCustomizedOptionWithDetails_Success() {
        Long id = 1L;
        CustomizedOptionDto optionDto = new CustomizedOptionDto();
        when(customizedOptionService.getCustomizedOptionWithDetails(id)).thenReturn(optionDto);

        ResponseEntity<?> response = customizedOptionController.getCustomizedOptionWithDetails(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(optionDto, response.getBody());
    }

    @Test
    void getCustomizedOptionWithDetails_NotFound() {
        Long id = 1L;
        when(customizedOptionService.getCustomizedOptionWithDetails(id)).thenThrow(new RuntimeException("Option not found"));

        ResponseEntity<?> response = customizedOptionController.getCustomizedOptionWithDetails(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Option not found", response.getBody());
    }

    @Test
    void addCustomizedOption_Success() {
        Long productId = 1L;
        Long materialId = 1L;
        CustomizedOptionDto optionDto = new CustomizedOptionDto();
        when(customizedOptionService.addCustomizedOption(optionDto, productId, materialId)).thenReturn(optionDto);

        ResponseEntity<?> response = customizedOptionController.addCustomizedOption(optionDto, productId, materialId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(optionDto, response.getBody());
    }

    @Test
    void addCustomizedOption_Exception() {
        Long productId = 1L;
        Long materialId = 1L;
        CustomizedOptionDto optionDto = new CustomizedOptionDto();
        when(customizedOptionService.addCustomizedOption(optionDto, productId, materialId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedOptionController.addCustomizedOption(optionDto, productId, materialId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void updateCustomizedOption_Success() {
        Long id = 1L;
        CustomizedOptionDto optionDto = new CustomizedOptionDto();
        when(customizedOptionService.updateCustomizedOption(id, optionDto)).thenReturn(optionDto);

        ResponseEntity<?> response = customizedOptionController.updateCustomizedOption(id, optionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(optionDto, response.getBody());
    }

    @Test
    void updateCustomizedOption_NotFound() {
        Long id = 1L;
        CustomizedOptionDto optionDto = new CustomizedOptionDto();
        when(customizedOptionService.updateCustomizedOption(id, optionDto)).thenThrow(new RuntimeException("Option not found"));

        ResponseEntity<?> response = customizedOptionController.updateCustomizedOption(id, optionDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Option not found", response.getBody());
    }

    @Test
    void deleteCustomizedOption_Success() {
        Long id = 1L;
        doNothing().when(customizedOptionService).deleteCustomizedOption(id);

        ResponseEntity<?> response = customizedOptionController.deleteCustomizedOption(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizedOption_NotFound() {
        Long id = 1L;
        doThrow(new RuntimeException("Option not found")).when(customizedOptionService).deleteCustomizedOption(id);

        ResponseEntity<?> response = customizedOptionController.deleteCustomizedOption(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Option not found", response.getBody());
    }
}