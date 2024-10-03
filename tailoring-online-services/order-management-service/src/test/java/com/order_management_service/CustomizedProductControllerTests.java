package com.order_management_service;

import com.order_management_service.controller.CustomizedProductController;
import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.model.CustomizedProduct;
import com.order_management_service.service.elasticsearch.CustomizedProductElasticsearchService;
import com.order_management_service.service.jpa.CustomizedProductJpaService;
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

class CustomizedProductControllerTests {

    @Mock
    private CustomizedProductJpaService customizedProductService;

    @Mock
    private CustomizedProductElasticsearchService elasticsearchService;

    @InjectMocks
    private CustomizedProductController customizedProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizedProducts_Success() {
        List<CustomizedProduct> products = Arrays.asList(new CustomizedProduct(), new CustomizedProduct());
        Page<CustomizedProduct> productPage = new PageImpl<>(products);
        when(customizedProductService.getAllCustomizedProducts(0, 9, "id", "asc")).thenReturn(productPage);

        ResponseEntity<?> response = customizedProductController.getAllCustomizedProducts(0, 9, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productPage, response.getBody());
    }

    @Test
    void getAllCustomizedProducts_Exception() {
        when(customizedProductService.getAllCustomizedProducts(0, 9, "id", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedProductController.getAllCustomizedProducts(0, 9, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getCustomizedProductById_Success() {
        Long id = 1L;
        CustomizedProduct product = new CustomizedProduct();
        when(customizedProductService.getCustomizedProductById(id)).thenReturn(product);

        ResponseEntity<?> response = customizedProductController.getCustomizedProductById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void getCustomizedProductById_NotFound() {
        Long id = 1L;
        when(customizedProductService.getCustomizedProductById(id)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<?> response = customizedProductController.getCustomizedProductById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    void getCustomizedProductWithProduct_Success() {
        Long id = 1L;
        CustomizedProductDto productDto = new CustomizedProductDto();
        when(customizedProductService.getCustomizedProductWithProduct(id)).thenReturn(productDto);

        ResponseEntity<?> response = customizedProductController.getCustomizedProductWithProduct(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void getCustomizedProductWithProduct_NotFound() {
        Long id = 1L;
        when(customizedProductService.getCustomizedProductWithProduct(id)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<?> response = customizedProductController.getCustomizedProductWithProduct(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    void addCustomizedProduct_Success() {
        Long productId = 1L;
        CustomizedProductDto productDto = new CustomizedProductDto();
        when(customizedProductService.addCustomizedProduct(productDto, productId)).thenReturn(productDto);

        ResponseEntity<?> response = customizedProductController.addCustomizedProduct(productDto, productId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void addCustomizedProduct_Exception() {
        Long productId = 1L;
        CustomizedProductDto productDto = new CustomizedProductDto();
        when(customizedProductService.addCustomizedProduct(productDto, productId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedProductController.addCustomizedProduct(productDto, productId);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void updateCustomizedProduct_Success() {
        Long id = 1L;
        CustomizedProductDto productDto = new CustomizedProductDto();
        when(customizedProductService.updateCustomizedProduct(id, productDto)).thenReturn(productDto);

        ResponseEntity<?> response = customizedProductController.updateCustomizedProduct(id, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDto, response.getBody());
    }

    @Test
    void updateCustomizedProduct_NotFound() {
        Long id = 1L;
        CustomizedProductDto productDto = new CustomizedProductDto();
        when(customizedProductService.updateCustomizedProduct(id, productDto)).thenThrow(new RuntimeException("Product not found"));

        ResponseEntity<?> response = customizedProductController.updateCustomizedProduct(id, productDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    @Test
    void deleteCustomizedProduct_Success() {
        Long id = 1L;
        doNothing().when(customizedProductService).deleteCustomizedProduct(id);

        ResponseEntity<?> response = customizedProductController.deleteCustomizedProduct(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteCustomizedProduct_NotFound() {
        Long id = 1L;
        doThrow(new RuntimeException("Product not found")).when(customizedProductService).deleteCustomizedProduct(id);

        ResponseEntity<?> response = customizedProductController.deleteCustomizedProduct(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Product not found", response.getBody());
    }

    // New Tests for Elasticsearch methods

    @Test
    void searchCustomizedProducts_Success() {
        String input = "test";
        List<CustomizedProduct> products = Arrays.asList(new CustomizedProduct(), new CustomizedProduct());
        when(elasticsearchService.search(input, 0, 10, "id", "asc")).thenReturn(new PageImpl<>(products));

        ResponseEntity<?> response = customizedProductController.search(input, 0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PageImpl<>(products), response.getBody());
    }

    @Test
    void searchCustomizedProducts_NotFound() {
        String input = "test";
        when(elasticsearchService.search(input, 0, 10, "id", "asc")).thenThrow(new RuntimeException("No results found"));

        ResponseEntity<?> response = customizedProductController.search(input, 0, 10, "id", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No results found", response.getBody());
    }

    @Test
    void filterCustomizedProducts_Success() {
        List<CustomizedProduct> products = Arrays.asList(new CustomizedProduct(), new CustomizedProduct());
        when(elasticsearchService.filter(0, 10, "id", "asc", "productIdFilter", "measurementFilter", "optionFilter"))
                .thenReturn(new PageImpl<>(products));

        ResponseEntity<?> response = customizedProductController.filter(0, 10, "id", "asc", "productIdFilter", "measurementFilter", "optionFilter");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(new PageImpl<>(products), response.getBody());
    }

    @Test
    void filterCustomizedProducts_NotFound() {
        when(elasticsearchService.filter(0, 10, "id", "asc", "productIdFilter", "measurementFilter", "optionFilter"))
                .thenThrow(new RuntimeException("No results found"));

        ResponseEntity<?> response = customizedProductController.filter(0, 10, "id", "asc", "productIdFilter", "measurementFilter", "optionFilter");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No results found", response.getBody());
    }

    @Test
    void autocompleteCustomizedProducts_Success() {
        List<String> suggestions = Arrays.asList("suggestion1", "suggestion2");
        when(elasticsearchService.autocomplete("test")).thenReturn(suggestions);

        ResponseEntity<?> response = customizedProductController.autocomplete("test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suggestions, response.getBody());
    }

    @Test
    void autocompleteCustomizedProducts_NotFound() {
        when(elasticsearchService.autocomplete("test")).thenThrow(new RuntimeException("No suggestions found"));

        ResponseEntity<?> response = customizedProductController.autocomplete("test");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No suggestions found", response.getBody());
    }
}
