package com.user_management_service.service;

import com.user_management_service.dto.AddressDto;
import com.user_management_service.mapper.AddressMapper;
import com.user_management_service.repository.AddressRepository;
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

    public List<AddressDto> getAllAddresses() {
        var addresses = addressRepository.findAll();
        return addressMapper.toDtoList(addresses);
    }

    public AddressDto getAddressById(Long id) {
        var address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        return addressMapper.toDto(address);
    }

    public AddressDto addAddress(AddressDto addressDto) {
        var address = addressMapper.toEntity(addressDto);
        var savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    public AddressDto updateAddress(Long id, AddressDto addressDto) {
        var existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        var updatedAddress = addressMapper.partialUpdate(addressDto, existingAddress);
        var savedAddress = addressRepository.save(updatedAddress);
        return addressMapper.toDto(savedAddress);
    }

    public void deleteAddress(Long id) {
        var address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }
}
