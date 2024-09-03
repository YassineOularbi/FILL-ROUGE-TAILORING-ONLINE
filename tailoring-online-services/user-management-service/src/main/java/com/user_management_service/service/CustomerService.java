package com.user_management_service.service;

import com.user_management_service.dto.CustomerDto;
import com.user_management_service.mapper.UserMapper;
import com.user_management_service.model.Customer;
import com.user_management_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserMapper customerMapper;
    private final PasswordEncoder passwordEncoder;
    private final KeycloakService keycloakService;
    private final UserService userService;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public CustomerDto register(CustomerDto customerDto) {
        try {
            userService.getUserByUsername(customerDto.getUsername());
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
        var customer = (Customer) customerMapper.toEntity(customerDto);
        String keycloakUserId;
        try {
            keycloakUserId = keycloakService.addUser(customer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create customer in Keycloak", e);
        }
        if (keycloakUserId == null || keycloakUserId.isEmpty()) {
            throw new RuntimeException("Failed to retrieve user ID from Keycloak");
        }
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        customer.setId(keycloakUserId);
        try {
            var savedCustomer= customerRepository.save(customer);
            return (CustomerDto) customerMapper.toDto(savedCustomer);
        } catch (Exception e) {
            keycloakService.deleteUser(keycloakUserId);
            throw new RuntimeException("Failed to save customer in local repository", e);
        }
    }

    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
        var existingCustomer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
        var updatedCustomer = (Customer) customerMapper.partialUpdate(customerDto, existingCustomer);
        try {
            keycloakService.updateUser(id, customerDto);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update customer in keycloak", e);
        }
        try {
            updatedCustomer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
            var savedcustomer = customerRepository.save(updatedCustomer);
            return (CustomerDto) customerMapper.toDto(savedcustomer);
        } catch (Exception e) {
            keycloakService.deleteUser(id);
            throw new RuntimeException("Failed to update customer in local repository", e);
        }
    }

    public void deleteCustomer(String id) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("customer not found"));
        try {
            keycloakService.deleteUser(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete customer in keycloak", e);
        }
        try {
            customerRepository.delete(customer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete customer in local repository", e);
        }
    }
}
