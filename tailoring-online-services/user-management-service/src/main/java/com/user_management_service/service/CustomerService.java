package com.user_management_service.service;

import com.user_management_service.dto.CustomerDto;
import com.user_management_service.mapper.CustomerMapper;
import com.user_management_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    public List<CustomerDto> getAllCustomers() {
        var customers = customerRepository.findAll();
        return customerMapper.toDtoList(customers);
    }

    public CustomerDto getCustomerById(Long id) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerMapper.toDto(customer);
    }

    public CustomerDto register(CustomerDto customerDto) {
        var customer = customerMapper.toEntity(customerDto);
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        var savedCustomer = customerRepository.save(customer);
        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        var existingCustomer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        var updatedCustomer = customerMapper.partialUpdate(customerDto, existingCustomer);
        updatedCustomer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        var savedCustomer = customerRepository.save(updatedCustomer);
        return customerMapper.toDto(savedCustomer);
    }

    public void deleteCustomer(Long id) {
        var customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }
}
