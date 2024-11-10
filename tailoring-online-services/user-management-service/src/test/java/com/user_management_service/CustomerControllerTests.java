//package com.user_management_service;
//
//import com.user_management_service.controller.CustomerController;
//import com.user_management_service.exception.CustomerNotFoundException;
//import com.user_management_service.model.Customer;
//import com.user_management_service.service.CustomerService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CustomerControllerTests {
//
//    @Mock
//    private CustomerService customerService;
//
//    @InjectMocks
//    private CustomerController customerController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void getAllCustomers_Success() {
//        List<Customer> customers = Arrays.asList(new Customer(), new Customer());
//        when(customerService.getAllCustomers()).thenReturn(customers);
//
//        ResponseEntity<?> response = customerController.getAllCustomers();
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(customers, response.getBody());
//    }
//
//    @Test
//    void getAllCustomers_Exception() {
//        when(customerService.getAllCustomers()).thenThrow(new RuntimeException("Error"));
//
//        ResponseEntity<?> response = customerController.getAllCustomers();
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals("Error", response.getBody());
//    }
//
//    @Test
//    void getCustomerById_Success() {
//        String id = "1";
//        Customer customer = new Customer();
//        when(customerService.getCustomerById(id)).thenReturn(customer);
//
//        ResponseEntity<?> response = customerController.getCustomerById(id);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(customer, response.getBody());
//    }
//
//    @Test
//    void getCustomerById_NotFound() {
//        String id = "1";
//        when(customerService.getCustomerById(id)).thenThrow(new CustomerNotFoundException(id));
//
//        ResponseEntity<?> response = customerController.getCustomerById(id);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(new CustomerNotFoundException(id).getMessage(), response.getBody());
//    }
//
//    @Test
//    void updateCustomer_Success() {
//        String id = "1";
//        CustomerDto customerDto = new CustomerDto();
//        when(customerService.updateCustomer(id, customerDto)).thenReturn(customerDto);
//
//        ResponseEntity<?> response = customerController.updateCustomer(id, customerDto);
//
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(customerDto, response.getBody());
//    }
//
//    @Test
//    void updateCustomer_NotFound() {
//        String id = "1";
//        CustomerDto customerDto = new CustomerDto();
//        when(customerService.updateCustomer(id, customerDto)).thenThrow(new CustomerNotFoundException(id));
//
//        ResponseEntity<?> response = customerController.updateCustomer(id, customerDto);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(new CustomerNotFoundException(id).getMessage(), response.getBody());
//    }
//
//    @Test
//    void deleteCustomer_Success() {
//        String id = "1";
//        doNothing().when(customerService).deleteCustomer(id);
//
//        ResponseEntity<?> response = customerController.deleteCustomer(id);
//
//        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        assertNull(response.getBody());
//    }
//
//    @Test
//    void deleteCustomer_NotFound() {
//        String id = "1";
//        doThrow(new CustomerNotFoundException(id)).when(customerService).deleteCustomer(id);
//
//        ResponseEntity<?> response = customerController.deleteCustomer(id);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(new CustomerNotFoundException(id).getMessage(), response.getBody());
//    }
//}
