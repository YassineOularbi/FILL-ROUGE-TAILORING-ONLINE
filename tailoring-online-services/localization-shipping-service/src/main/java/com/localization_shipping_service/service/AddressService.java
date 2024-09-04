package com.localization_shipping_service.service;

import com.localization_shipping_service.client.UserManagementClient;
import com.localization_shipping_service.dto.AddressDto;
import com.localization_shipping_service.exception.AddressNotFoundException;
import com.localization_shipping_service.exception.UserNotFoundException;
import com.localization_shipping_service.mapper.AddressMapper;
import com.localization_shipping_service.model.Address;
import com.localization_shipping_service.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserManagementClient userManagementClient;

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
    }
    
    public AddressDto addAddress(AddressDto addressDto, String id) {
        var user = userManagementClient.getUserById(id);
        if (user.getStatusCode().is2xxSuccessful()){
            var address = addressRepository.findByUserId(id);
            if (address.isPresent()){
                 return updateAddress(address.get().getId(), addressDto);
            } else {
                var mappedAddress = addressMapper.toEntity(addressDto);
                mappedAddress.setUserId(id);
                var savedAddress = addressRepository.save(mappedAddress);
                return addressMapper.toDto(savedAddress);
            }
        } else {
            throw new UserNotFoundException(id);
        }
    }

    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        var existingAddress = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        var updatedAddress = addressMapper.partialUpdate(addressDto, existingAddress);
        var savedAddress = addressRepository.save(updatedAddress);
        return addressMapper.toDto(savedAddress);
    }

    public void deleteAddress(Long id) {
        var address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
        addressRepository.delete(address);
    }
}
