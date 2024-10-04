package com.localization_shipping_service.service.jpa;

import com.localization_shipping_service.client.UserManagementClient;
import com.localization_shipping_service.dto.AddressDto;
import com.localization_shipping_service.exception.AddressNotFoundException;
import com.localization_shipping_service.exception.UserNotFoundException;
import com.localization_shipping_service.mapper.AddressMapper;
import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.repository.jpa.AddressJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressJpaService {

    private static final Logger logger = LoggerFactory.getLogger(AddressJpaService.class);
    private final AddressJpaRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserManagementClient userManagementClient;
    private static final int MAX_PAGE_SIZE = 100;

    public Page<Address> getAllAddresses(int page, int size, String sortField, String sortDirection) {
        logger.info("Fetching all addresses with page: {}, size: {}, sortField: {}, sortDirection: {}", page, size, sortField, sortDirection);

        if (size > MAX_PAGE_SIZE) {
            size = MAX_PAGE_SIZE;
            logger.warn("Requested size exceeds maximum page size. Setting size to {}", MAX_PAGE_SIZE);
        }
        Sort.Direction direction = sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortField));
        Page<Address> addresses = addressRepository.findAll(pageable);

        logger.info("Fetched {} addresses", addresses.getTotalElements());
        return addresses;
    }

    public Address getAddressById(Long id) {
        logger.info("Fetching address with ID: {}", id);
        return addressRepository.findById(id).orElseThrow(() -> {
            logger.error("Address not found with ID: {}", id);
            return new AddressNotFoundException(id);
        });
    }

    public AddressDto getAddressWithUser(Long id) {
        logger.info("Fetching address with user for ID: {}", id);
        var address = addressRepository.findById(id).orElseThrow(() -> {
            logger.error("Address not found with ID: {}", id);
            return new AddressNotFoundException(id);
        });
        var user = userManagementClient.getUserById(address.getUserId()).orElseThrow(() -> {
            logger.error("User not found with ID: {}", address.getUserId());
            return new UserNotFoundException(address.getUserId());
        });
        var mappedAddress = addressMapper.toDto(address);
        mappedAddress.setUser(user);
        logger.info("Fetched address with user successfully for ID: {}", id);
        return mappedAddress;
    }

    public AddressDto addAddress(AddressDto addressDto, String id) {
        logger.info("Adding address for user ID: {}", id);
        var user = userManagementClient.getUserById(id).orElseThrow(() -> {
            logger.error("User not found with ID: {}", id);
            return new UserNotFoundException(id);
        });
        var address = addressRepository.findByUserId(id);
        if (address.isPresent()) {
            logger.info("Address already exists for user ID: {}, updating it.", id);
            return updateAddress(address.get().getId(), addressDto);
        } else {
            var mappedAddress = addressMapper.toEntity(addressDto);
            mappedAddress.setUserId(user.getId());
            var savedAddress = addressRepository.save(mappedAddress);
            logger.info("Address added successfully for user ID: {}", id);
            return addressMapper.toDto(savedAddress);
        }
    }

    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        logger.info("Updating address with ID: {}", id);
        var existingAddress = addressRepository.findById(id).orElseThrow(() -> {
            logger.error("Address not found with ID: {}", id);
            return new AddressNotFoundException(id);
        });
        var updatedAddress = addressMapper.partialUpdate(addressDto, existingAddress);
        var savedAddress = addressRepository.save(updatedAddress);
        logger.info("Address updated successfully with ID: {}", id);
        return addressMapper.toDto(savedAddress);
    }

    public void deleteAddress(Long id) {
        logger.info("Deleting address with ID: {}", id);
        var address = addressRepository.findById(id).orElseThrow(() -> {
            logger.error("Address not found with ID: {}", id);
            return new AddressNotFoundException(id);
        });
        addressRepository.delete(address);
        logger.info("Address deleted successfully with ID: {}", id);
    }
}
