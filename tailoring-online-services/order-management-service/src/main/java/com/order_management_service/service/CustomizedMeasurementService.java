package com.order_management_service.service;

import com.order_management_service.client.StoreManagementClient;
import com.order_management_service.dto.CustomizedMeasurementDto;
import com.order_management_service.exception.CustomizedMeasurementNotFoundException;
import com.order_management_service.exception.CustomizedProductNotFoundException;
import com.order_management_service.exception.MeasurementNotFoundException;
import com.order_management_service.mapper.CustomizedMeasurementMapper;
import com.order_management_service.model.CustomizedMeasurement;
import com.order_management_service.repository.CustomizedMeasurementRepository;
import com.order_management_service.repository.CustomizedProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizedMeasurementService {

    private final CustomizedMeasurementRepository customizedMeasurementRepository;
    private final CustomizedMeasurementMapper customizedMeasurementMapper;
    private final StoreManagementClient storeManagementClient;
    private final CustomizedProductRepository customizedProductRepository;

    public List<CustomizedMeasurement> getAllCustomizedMeasurements() {
        return customizedMeasurementRepository.findAll();
    }

    public CustomizedMeasurement getCustomizedMeasurementById(Long id) {
        return customizedMeasurementRepository.findById(id).orElseThrow(() -> new CustomizedMeasurementNotFoundException(id));
    }

    public CustomizedMeasurementDto getCustomizedMeasurementWithDetails(Long id) {
        var customizedMeasurement = customizedMeasurementRepository.findById(id).orElseThrow(() -> new CustomizedMeasurementNotFoundException(id));
        var measurement = storeManagementClient.getMeasurementById(String.valueOf(customizedMeasurement.getMeasurementId())).orElseThrow(() -> new MeasurementNotFoundException(customizedMeasurement.getMeasurementId()));
        var mappedMeasurement = customizedMeasurementMapper.toDto(customizedMeasurement);
        mappedMeasurement.setMeasurement(measurement);
        return mappedMeasurement;
    }

    public CustomizedMeasurementDto addCustomizedMeasurement(CustomizedMeasurementDto customizedMeasurementDto, Long customizedProductId, Long measurementId) {
        var customizedProduct = customizedProductRepository.findById(customizedProductId).orElseThrow(() -> new CustomizedProductNotFoundException(customizedProductId));
        var measurement = storeManagementClient.getMeasurementById(String.valueOf(measurementId)).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var customizedMeasurement = customizedMeasurementRepository.findByMeasurementIdAndProductId(measurement.getId(), customizedProduct.getProductId());
        if (customizedMeasurement.isPresent()) {
            return updateCustomizedMeasurement(customizedMeasurement.get().getId(), customizedMeasurementDto);
        } else {
            var mappedMeasurement = customizedMeasurementMapper.toEntity(customizedMeasurementDto);
            mappedMeasurement.setMeasurementId(measurement.getId());
            mappedMeasurement.setProduct(customizedProduct);
            var savedMeasurement = customizedMeasurementRepository.save(mappedMeasurement);
            return customizedMeasurementMapper.toDto(savedMeasurement);
        }
    }

    public CustomizedMeasurementDto updateCustomizedMeasurement(Long id, CustomizedMeasurementDto customizedMeasurementDto) {
        var existingCustomizedMeasurement = customizedMeasurementRepository.findById(id).orElseThrow(() -> new CustomizedMeasurementNotFoundException(id));
        var updatedMeasurement = customizedMeasurementMapper.partialUpdate(customizedMeasurementDto, existingCustomizedMeasurement);
        var savedMeasurement = customizedMeasurementRepository.save(updatedMeasurement);
        return customizedMeasurementMapper.toDto(savedMeasurement);
    }

    public void deleteCustomizedMeasurement(Long id) {
        var customizedMeasurement = customizedMeasurementRepository.findById(id).orElseThrow(() -> new CustomizedMeasurementNotFoundException(id));
        customizedMeasurementRepository.delete(customizedMeasurement);
    }
}
