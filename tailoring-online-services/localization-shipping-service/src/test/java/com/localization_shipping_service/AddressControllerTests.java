package com.localization_shipping_service;

import com.localization_shipping_service.controller.AddressController;
import com.localization_shipping_service.dto.AddressDto;
import com.localization_shipping_service.exception.AddressNotFoundException;
import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.service.jpa.AddressJpaService;
import com.localization_shipping_service.service.elasticsearch.AddressElasticsearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressControllerTests {

    @Mock
    private AddressJpaService addressService;

    @Mock
    private AddressElasticsearchService elasticsearchService;

    @InjectMocks
    private AddressController addressController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAddresses_Success() {
        List<Address> addresses = List.of(new Address(), new Address());

        Page<Address> addressPage = new PageImpl<>(addresses, PageRequest.of(0, 2), addresses.size());

        when(addressService.getAllAddresses(0, 9, "address", "asc")).thenReturn(addressPage);

        ResponseEntity<?> response = addressController.getAllAddresses(0, 9, "address", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressPage, response.getBody());
    }

    @Test
    void getAllAddresses_Exception() {
        when(addressService.getAllAddresses(anyInt(), anyInt(), anyString(), anyString())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = addressController.getAllAddresses(0, 9, "address", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void getAddressById_Success() {
        Long addressId = 1L;
        Address address = new Address();
        when(addressService.getAddressById(addressId)).thenReturn(address);

        ResponseEntity<?> response = addressController.getAddressById(String.valueOf(addressId));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(address, response.getBody());
    }

    @Test
    void getAddressById_NotFound() {
        Long addressId = 1L;
        when(addressService.getAddressById(addressId)).thenThrow(new AddressNotFoundException(addressId));

        ResponseEntity<?> response = addressController.getAddressById(String.valueOf(addressId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new AddressNotFoundException(addressId).getMessage(), response.getBody());
    }

    @Test
    void addAddress_Success() {
        AddressDto addressDto = new AddressDto();
        AddressDto createdAddress = new AddressDto();
        when(addressService.addAddress(addressDto, "1")).thenReturn(createdAddress);

        ResponseEntity<?> response = addressController.addAddress(addressDto, "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdAddress, response.getBody());
    }

    @Test
    void addAddress_Exception() {
        AddressDto addressDto = new AddressDto();
        when(addressService.addAddress(addressDto, "1")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = addressController.addAddress(addressDto, "1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void updateAddress_Success() {
        Long addressId = 1L;
        AddressDto addressDto = new AddressDto();
        AddressDto updatedAddress = new AddressDto();
        when(addressService.updateAddress(addressId, addressDto)).thenReturn(updatedAddress);

        ResponseEntity<?> response = addressController.updateAddress(String.valueOf(addressId), addressDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAddress, response.getBody());
    }

    @Test
    void updateAddress_NotFound() {
        Long addressId = 1L;
        AddressDto addressDto = new AddressDto();
        when(addressService.updateAddress(addressId, addressDto)).thenThrow(new AddressNotFoundException(addressId));

        ResponseEntity<?> response = addressController.updateAddress(String.valueOf(addressId), addressDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new AddressNotFoundException(addressId).getMessage(), response.getBody());
    }

    @Test
    void deleteAddress_Success() {
        Long addressId = 1L;
        doNothing().when(addressService).deleteAddress(addressId);

        ResponseEntity<?> response = addressController.deleteAddress(String.valueOf(addressId));

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteAddress_NotFound() {
        Long addressId = 1L;
        doThrow(new AddressNotFoundException(addressId)).when(addressService).deleteAddress(addressId);

        ResponseEntity<?> response = addressController.deleteAddress(String.valueOf(addressId));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(new AddressNotFoundException(addressId).getMessage(), response.getBody());
    }

    @Test
    void search_Success() {
        String input = "test";
        List<Address> addresses = List.of(new Address(), new Address());

        Page<Address> addressPage = new PageImpl<>(addresses, PageRequest.of(0, 2), addresses.size());

        when(elasticsearchService.search(input, 0, 9, "address", "asc")).thenReturn(addressPage);

        ResponseEntity<?> response = addressController.search(input, 0, 9, "address", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressPage, response.getBody());
    }

    @Test
    void search_NotFound() {
        String input = "test";
        when(elasticsearchService.search(input, 0, 9, "address", "asc")).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = addressController.search(input, 0, 9, "address", "asc");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void filter_Success() {
        List<Address> addresses = List.of(new Address(), new Address());

        Page<Address> addressPage = new PageImpl<>(addresses, PageRequest.of(0, 2), addresses.size());

        when(elasticsearchService.filter(0, 10, "city", "asc", null, null, null, null, null, null, null)).thenReturn(addressPage);

        ResponseEntity<?> response = addressController.filter(0, 10, "city", "asc", null, null, null, null, null, null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(addressPage, response.getBody());
    }

    @Test
    void filter_NotFound() {
        when(elasticsearchService.filter(anyInt(), anyInt(), anyString(), anyString(), any(), any(), any(), any(), any(), any(), any())).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = addressController.filter(0, 10, "city", "asc", null, null, null, null, null, null, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }

    @Test
    void autocomplete_Success() {
        String input = "Main";
        List<String> suggestions = List.of("123 Main St", "456 Main St");
        when(elasticsearchService.autocomplete(input)).thenReturn(suggestions);

        ResponseEntity<?> response = addressController.autocomplete(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(suggestions, response.getBody());
    }

    @Test
    void autocomplete_NotFound() {
        String input = "Main";
        when(elasticsearchService.autocomplete(input)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<?> response = addressController.autocomplete(input);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Error", response.getBody());
    }
}
