package com.localization_shipping_service.controller;

import com.localization_shipping_service.dto.AddressDto;
import com.localization_shipping_service.service.elasticsearch.AddressElasticsearchService;
import com.localization_shipping_service.service.jpa.AddressJpaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressJpaService addressService;
    private final AddressElasticsearchService elasticsearchService;

    @GetMapping("/get-all-addresses")
    public ResponseEntity<?> getAllAddresses(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "9", name = "size") int size,
            @RequestParam(defaultValue = "address", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var addresses = addressService.getAllAddresses(page, size, sortField, sortDirection);
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

    @GetMapping("/get-address-with-user/{id}")
    public ResponseEntity<?> getAddressWithUser(@PathVariable("id") String id) {
        try {
            var address = addressService.getAddressWithUser(Long.valueOf(id));
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

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam("input") String input,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "9", name = "size") int size,
            @RequestParam(defaultValue = "address", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        try {
            var addresses = elasticsearchService.search(input, page, size, sortField, sortDirection);
            return ResponseEntity.ok(addresses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "city", name = "sortField") String sortField,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection,
            @RequestParam(required = false) String addressFilter,
            @RequestParam(required = false) String suiteFilter,
            @RequestParam(required = false) String cityFilter,
            @RequestParam(required = false) String provinceFilter,
            @RequestParam(required = false) String countryFilter,
            @RequestParam(required = false) Boolean defaultFilter,
            @RequestParam(required = false) String zipCodeFilter) {
        try {
            var addresses = elasticsearchService.filter(page, size, sortField, sortDirection, addressFilter, suiteFilter, cityFilter, provinceFilter, countryFilter, defaultFilter, zipCodeFilter);
            return ResponseEntity.ok(addresses);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
