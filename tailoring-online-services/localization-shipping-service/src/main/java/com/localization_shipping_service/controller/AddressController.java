package com.localization_shipping_service.controller;

import com.localization_shipping_service.dto.AddressDto;
import com.localization_shipping_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/get-all-addresses")
    public ResponseEntity<?> getAllAddresses() {
        try {
            var addresses = addressService.getAllAddresses();
            return ResponseEntity.ok(addresses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/get-address-by-id/{id}")
    public ResponseEntity<?> getAddressById(@PathVariable("id") String id) {
        try {
            var address = addressService.getAddressById(Long.valueOf(id));
            return ResponseEntity.ok(address);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/add-address/{id}")
    public ResponseEntity<?> addAddress(@RequestBody AddressDto addressDto, @PathVariable("id") String id) {
        try {
            var addedAddress = addressService.addAddress(addressDto, id);
            return ResponseEntity.ok(addedAddress);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/update-address/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable("id") String id, @RequestBody AddressDto addressDto) {
        try {
            var updatedAddress = addressService.updateAddress(Long.valueOf(id), addressDto);
            return ResponseEntity.ok(updatedAddress);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete-address/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable("id") String id) {
        try {
            addressService.deleteAddress(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
