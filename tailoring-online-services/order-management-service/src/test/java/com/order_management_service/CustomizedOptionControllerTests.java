package com.order_management_service;

import com.order_management_service.controller.CustomizedOptionController;
import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.model.CustomizedOption;
import com.order_management_service.service.elasticsearch.CustomizedOptionElasticsearchService;
import com.order_management_service.service.jpa.CustomizedOptionJpaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomizedOptionControllerTests {

    @Mock
    private CustomizedOptionJpaService customizedOptionService;

    @Mock
    private CustomizedOptionElasticsearchService elasticsearchService;

    @InjectMocks
    private CustomizedOptionController customizedOptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizedOptions_Success() {
        List<CustomizedOption> options = Arrays.asList(new CustomizedOption(), new CustomizedOption());
        Page<CustomizedOption> optionPage = new PageImpl<>(options);
        when(customizedOptionService.getAllCustomizedOptions(0, 9, "id", "asc")).thenReturn(optionPage);

        ResponseEntity<?> response = customizedOptionController.getAllCustomizedOptions(0, 9, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(optionPage, response.getBody());
    }

    @Test
    void getAllCustomizedOptions_Exception() {
        when(customizedOptionService.getAllCustomizedOptions(0, 9, "id", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedOptionController.getAllCustomizedOptions(0, 9, "id", "asc");

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

    // New Tests for Elasticsearch methods

    @Test
    void searchCustomizedOptions_Success() {
        String input = "test";
        List<CustomizedOption> options = Arrays.asList(new CustomizedOption(), new CustomizedOption());
        when(elasticsearchService.search(input, 0, 10, "id", "asc")).thenReturn(new PageImpl<>(options));

        ResponseEntity<?> response = customizedOptionController.search(input, 0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PageImpl<>(options), response.getBody());
    }

    @Test
    void searchCustomizedOptions_NotFound() {
        String input = "test";
        when(elasticsearchService.search(input, 0, 10, "id", "asc")).thenThrow(new RuntimeException("No results found"));

        ResponseEntity<?> response = customizedOptionController.search(input, 0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No results found", response.getBody());
    }

    @Test
    void filterCustomizedOptions_Success() {
        List<CustomizedOption> options = Arrays.asList(new CustomizedOption(), new CustomizedOption());
        when(elasticsearchService.filter(0, 10, "id", "asc", "type", 1L, 1L)).thenReturn(new PageImpl<>(options));

        ResponseEntity<?> response = customizedOptionController.filter(0, 10, "id", "asc", "type", 1L, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PageImpl<>(options), response.getBody());
    }

    @Test
    void filterCustomizedOptions_NotFound() {
        when(elasticsearchService.filter(0, 10, "id", "asc", "type", 1L, 1L)).thenThrow(new RuntimeException("No results found"));

        ResponseEntity<?> response = customizedOptionController.filter(0, 10, "id", "asc", "type", 1L, 1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No results found", response.getBody());
    }

    @Test
    void autocompleteCustomizedOptions_Success() {
        List<String> suggestions = Arrays.asList("suggestion1", "suggestion2");
        when(elasticsearchService.autocomplete("test")).thenReturn(suggestions);

        ResponseEntity<?> response = customizedOptionController.autocomplete("test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suggestions, response.getBody());
    }

    @Test
    void autocompleteCustomizedOptions_NotFound() {
        when(elasticsearchService.autocomplete("test")).thenThrow(new RuntimeException("No suggestions found"));

        ResponseEntity<?> response = customizedOptionController.autocomplete("test");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No suggestions found", response.getBody());
    }
}
