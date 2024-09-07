package com.store_management_service.service;

import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.exception.MeasurementNotFoundException;
import com.store_management_service.exception.ThreeDModelNotFoundException;
import com.store_management_service.mapper.CustomizableMeasurementMapper;
import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableMeasurementKey;
import com.store_management_service.repository.CustomizableMeasurementRepository;
import com.store_management_service.repository.MeasurementRepository;
import com.store_management_service.repository.ThreeDModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomizableMeasurementService {

    private final CustomizableMeasurementRepository customizableMeasurementRepository;
    private final CustomizableMeasurementMapper customizableMeasurementMapper;
    private final ThreeDModelRepository threeDModelRepository;
    private final MeasurementRepository measurementRepository;

    public List<CustomizableMeasurement> getAllCustomizableMeasurement() {
        return customizableMeasurementRepository.findAll();
    }

    public List<CustomizableMeasurement> getAllCustomizableMeasurementByThreeDModel(Long id) {
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        return customizableMeasurementRepository.findAllByModel(threeDModel);
    }

    public CustomizableMeasurementDto addCustomizableMeasurement(Long threeDModelId, Long measurementId) {
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModelId, measurementId);
        var customizableMeasurement = new CustomizableMeasurement(id, threeDModel, measurement);
        var savedCustomizableMeasurement = customizableMeasurementRepository.save(customizableMeasurement);
        return customizableMeasurementMapper.toDto(savedCustomizableMeasurement);
    }
}
