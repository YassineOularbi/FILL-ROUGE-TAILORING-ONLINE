package com.store_management_service.service;

import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.exception.CustomizableMeasurementNotFoundException;
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

    public List<CustomizableMeasurementDto> getAllCustomizableMeasurement() {
        return customizableMeasurementMapper.toDtos(customizableMeasurementRepository.findAll());
    }

    public List<CustomizableMeasurement> getAllCustomizableMeasurementByThreeDModel(Long id) {
        var threeDModel = threeDModelRepository.findById(id).orElseThrow(() -> new ThreeDModelNotFoundException(id));
        return customizableMeasurementRepository.findAllByModel(threeDModel);
    }

    public CustomizableMeasurement getCustomizableMeasurementById(Long threeDModelId, Long measurementId){
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModel.getId(), measurement.getId());
        return customizableMeasurementRepository.findById(id).orElseThrow(() -> new CustomizableMeasurementNotFoundException(id));
    }

    public CustomizableMeasurementDto addCustomizableMeasurement(Long threeDModelId, Long measurementId) {
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModel.getId(), measurement.getId());
        var customizableMeasurement = new CustomizableMeasurement(id, threeDModel, measurement);
        var savedCustomizableMeasurement = customizableMeasurementRepository.save(customizableMeasurement);
        return customizableMeasurementMapper.toDto(savedCustomizableMeasurement);
    }

    public void deleteCustomizableMeasurement(Long threeDModelId, Long measurementId){
        var threeDModel = threeDModelRepository.findById(threeDModelId).orElseThrow(() -> new ThreeDModelNotFoundException(threeDModelId));
        var measurement = measurementRepository.findById(measurementId).orElseThrow(() -> new MeasurementNotFoundException(measurementId));
        var id = new CustomizableMeasurementKey(threeDModel.getId(), measurement.getId());
        var existingCustomizableMeasurement = customizableMeasurementRepository.findById(id).orElseThrow(() -> new CustomizableMeasurementNotFoundException(id));
        customizableMeasurementRepository.delete(existingCustomizableMeasurement);
    }
}
