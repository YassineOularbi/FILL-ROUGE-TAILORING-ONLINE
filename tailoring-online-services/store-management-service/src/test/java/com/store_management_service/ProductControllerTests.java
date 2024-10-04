package com.store_management_service;

import com.store_management_service.controller.ProductController;
import com.store_management_service.dto.ProductDto;
import com.store_management_service.exception.ProductNotFoundException;
import com.store_management_service.exception.StoreNotFoundException;
import com.store_management_service.model.Product;
import com.store_management_service.service.jpa.ProductJpaService;
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

class ProductControllerTests {

    @Mock
    private ProductJpaService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void getAllProducts_Success() {
//        List<Product> products = Arrays.asList(new Product(), new Product());
//        when(productService.getAllProducts()).thenReturn(products);
//
//        ResponseEntity<?> response = productController.getAllProducts();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(products, response.getBody());
//    }
//
//    @Test
//    void getAllProducts_Exception() {
//        when(productService.getAllProducts()).thenThrow(new RuntimeException("Error"));
//
//        ResponseEntity<?> response = productController.getAllProducts();
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Error", response.getBody());
//    }

    @Test
    void getAllProductsByStore_Success() {
        Long storeId = 1L;
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(productService.getAllProductsByStore(storeId)).thenReturn(products);

        ResponseEntity<?> response = productController.getAllProductsByStore(String.valueOf(storeId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void getAllProductsByStore_NotFound() {
        Long storeId = 1L;
        when(productService.getAllProductsByStore(storeId)).thenThrow(new StoreNotFoundException(storeId));

        ResponseEntity<?> response = productController.getAllProductsByStore(String.valueOf(storeId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new StoreNotFoundException(storeId).getMessage(), response.getBody());
    }

    @Test
    void getProductById_Success() {
        Long id = 1L;
        Product product = new Product();
        when(productService.getProductById(id)).thenReturn(product);

        ResponseEntity<?> response = productController.getProductById(String.valueOf(id));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void getProductById_NotFound() {
        Long id = 1L;
        when(productService.getProductById(id)).thenThrow(new ProductNotFoundException(id));

        ResponseEntity<?> response = productController.getProductById(String.valueOf(id));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ProductNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void addProduct_Success() {
        Long storeId = 1L;
        ProductDto productDto = new ProductDto();
        ProductDto addedProduct = new ProductDto();
        when(productService.addProduct(productDto, storeId)).thenReturn(addedProduct);

        ResponseEntity<?> response = productController.addProduct(productDto, String.valueOf(storeId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addedProduct, response.getBody());
    }

    @Test
    void addProduct_Exception() {
        Long storeId = 1L;
        ProductDto productDto = new ProductDto();
        when(productService.addProduct(productDto, storeId)).thenThrow(new StoreNotFoundException(storeId));

        ResponseEntity<?> response = productController.addProduct(productDto, String.valueOf(storeId));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(new StoreNotFoundException(storeId).getMessage(), response.getBody());
    }

    @Test
    void updateProduct_Success() {
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        ProductDto updatedProduct = new ProductDto();
        when(productService.updateProduct(id, productDto)).thenReturn(updatedProduct);

        ResponseEntity<?> response = productController.updateProduct(id, productDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProduct, response.getBody());
    }

    @Test
    void updateProduct_NotFound() {
        Long id = 1L;
        ProductDto productDto = new ProductDto();
        when(productService.updateProduct(id, productDto)).thenThrow(new ProductNotFoundException(id));

        ResponseEntity<?> response = productController.updateProduct(id, productDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ProductNotFoundException(id).getMessage(), response.getBody());
    }

    @Test
    void deleteProduct_Success() {
        Long id = 1L;
        doNothing().when(productService).deleteProduct(id);

        ResponseEntity<?> response = productController.deleteProduct(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteProduct_NotFound() {
        Long id = 1L;
        doThrow(new ProductNotFoundException(id)).when(productService).deleteProduct(id);

        ResponseEntity<?> response = productController.deleteProduct(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new ProductNotFoundException(id).getMessage(), response.getBody());
    }
}
