//package com.user_management_service.service;
//
//import com.user_management_service.dto.AddressDto;
//import com.user_management_service.exception.AddressNotFoundException;
//import com.user_management_service.exception.UserNotFoundException;
//import com.user_management_service.mapper.AddressMapper;
//import com.user_management_service.model.Address;
//import com.user_management_service.repository.AddressRepository;
//import com.user_management_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional
//@RequiredArgsConstructor
//public class AddressService {
//
//    private final AddressRepository addressRepository;
//    private final AddressMapper addressMapper;
//    private final UserRepository userRepository;
//
//    public List<Address> getAllAddresses() {
//        return addressRepository.findAll();
//    }
//
//    public Address getAddressById(Long id) {
//        return addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
//    }
//
//    public AddressDto addAddress(AddressDto addressDto, String id) {
//        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
//        var address = addressMapper.toEntity(addressDto);
//        address.setUser(user);
//        var savedAddress = addressRepository.save(address);
//        user.setAddress(savedAddress);
//        userRepository.save(user);
//        return addressMapper.toDto(savedAddress);
//    }
//
//    public AddressDto updateAddress(Long id, AddressDto addressDto) {
//        var existingAddress = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
//        var updatedAddress = addressMapper.partialUpdate(addressDto, existingAddress);
//        var savedAddress = addressRepository.save(updatedAddress);
//        return addressMapper.toDto(savedAddress);
//    }
//
//    public void deleteAddress(Long id) {
//        var address = addressRepository.findById(id).orElseThrow(() -> new AddressNotFoundException(id));
//        addressRepository.delete(address);
//    }
//}
