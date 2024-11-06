//package com.user_management_service.service;
//
//import com.user_management_service.dto.CustomerDto;
//import com.user_management_service.exception.CustomerNotFoundException;
//import com.user_management_service.exception.CustomerRepositoryException;
//import com.user_management_service.exception.KeycloakServiceException;
//import com.user_management_service.mapper.UserMapper;
//import com.user_management_service.model.Customer;
//import com.user_management_service.repository.CustomerRepository;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class CustomerService {
//
//    private final CustomerRepository customerRepository;
//    private final UserMapper customerMapper;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationService authenticationService;
//    private final UserService userService;
//
//    public List<Customer> getAllCustomers() {
//        return customerRepository.findAll();
//    }
//
//    public Customer getCustomerById(String id) {
//        return customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
//    }
//
//    public CustomerDto register(CustomerDto customerDto) {
//        Logger logger = LoggerFactory.getLogger(getClass());
//
//        logger.info("Registering customer: {}", customerDto);
//
//        userService.getUserByUsername(customerDto.getUsername());
//
//        var customer = (Customer) customerMapper.toEntity(customerDto);
//
//        logger.info("Customer password before adding to Keycloak: {}", customer.getPassword());
//
//        String keycloakUserId;
//        try {
//            keycloakUserId = authenticationService.addUser(customer);
//        } catch (Exception e) {
//            logger.error("Failed to create customer in Keycloak: {}", e.getMessage());
//            throw new KeycloakServiceException("Failed to create customer in Keycloak", e);
//        }
//
//        if (keycloakUserId == null || keycloakUserId.isEmpty()) {
//            logger.error("Failed to retrieve user ID from Keycloak");
//            throw new KeycloakServiceException("Failed to retrieve user ID from Keycloak");
//        }
//
//        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
//        customer.setId(keycloakUserId);
//
//        try {
//            var savedCustomer = customerRepository.save(customer);
//            logger.info("Customer saved successfully with ID: {}", savedCustomer.getId());
//            return (CustomerDto) customerMapper.toDto(savedCustomer);
//        } catch (Exception e) {
//            authenticationService.deleteUser(keycloakUserId);
//            logger.error("Failed to save customer in local repository: {}", e.getMessage());
//            throw new CustomerRepositoryException("Failed to save customer in local repository", e);
//        }
//    }
//
//
//    public CustomerDto updateCustomer(String id, CustomerDto customerDto) {
//        var existingCustomer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
//        var updatedCustomer = (Customer) customerMapper.partialUpdate(customerDto, existingCustomer);
//        try {
//            authenticationService.updateUser(id, customerDto);
//        } catch (Exception e) {
//            throw new KeycloakServiceException("Failed to update customer in keycloak", e);
//        }
//        try {
//            updatedCustomer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
//            var savedcustomer = customerRepository.save(updatedCustomer);
//            return (CustomerDto) customerMapper.toDto(savedcustomer);
//        } catch (Exception e) {
//            authenticationService.deleteUser(id);
//            throw new CustomerRepositoryException("Failed to update customer in local repository", e);
//        }
//    }
//
//    public void deleteCustomer(String id) {
//        var customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
//        try {
//            authenticationService.deleteUser(id);
//        } catch (Exception e) {
//            throw new KeycloakServiceException("Failed to delete customer in keycloak", e);
//        }
//        try {
//            customerRepository.delete(customer);
//        } catch (Exception e) {
//            throw new CustomerRepositoryException("Failed to delete customer in local repository", e);
//        }
//    }
//}
