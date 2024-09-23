package com.order_management_service;

import com.order_management_service.controller.CustomizedProductController;
import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.model.CustomizedProduct;
import com.order_management_service.service.CustomizedProductService;
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

public class CustomizedProductControllerTests {

    @Mock
    private CustomizedProductService customizedProductService;

    @InjectMocks
    private CustomizedProductController customizedProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCustomizedProducts_Success() {
        List<CustomizedProduct> products = Arrays.asList(new CustomizedProduct(), new CustomizedProduct());
        when(customizedProductService.getAllCustomizedProducts()).thenReturn(products);

        ResponseEntity<?> response = customizedProductController.getAllCustomizedProducts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void getAllCustomizedProducts_Exception() {
        when(customizedProductService.getAllCustomizedProducts()).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = customizedProductController.getAllCustomizedProducts();

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
}
