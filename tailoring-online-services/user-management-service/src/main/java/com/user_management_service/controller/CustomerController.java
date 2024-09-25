package com.user_management_service.controller;

import com.user_management_service.dto.CustomerDto;
import com.user_management_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/get-all-customers")
    public ResponseEntity<?> getAllCustomers() {
        try {
            var customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-customer-by-id/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable("id") String id) {
        try {
            var customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDto customerDto) {
        try {
            var addedCustomer = customerService.register(customerDto);
            return ResponseEntity.ok(addedCustomer);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable("id") String id, @RequestBody CustomerDto customerDto) {
        try {
            var updatedCustomer = customerService.updateCustomer(id, customerDto);
            return ResponseEntity.ok(updatedCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable("id") String id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
