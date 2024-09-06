package com.store_management_service.service;

import com.store_management_service.dto.MeasurementDto;
import com.store_management_service.exception.MeasurementNotFoundException;
import com.store_management_service.mapper.MeasurementMapper;
import com.store_management_service.model.Measurement;
import com.store_management_service.repository.MeasurementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final MeasurementMapper measurementMapper;

    public List<Measurement> getAllMeasurements() {
        return measurementRepository.findAll();
    }

    public Measurement getMeasurementById(Long id) {
        return measurementRepository.findById(id).orElseThrow(() -> new MeasurementNotFoundException(id));
    }

    public MeasurementDto addMeasurement(MeasurementDto measurementDto) {
        var measurement = measurementMapper.toEntity(measurementDto);
        var savedMeasurement = measurementRepository.save(measurement);
        return measurementMapper.toDto(savedMeasurement);
    }

    public MeasurementDto updateMeasurement(Long id, MeasurementDto measurementDto) {
        var existingMeasurement = measurementRepository.findById(id).orElseThrow(() -> new MeasurementNotFoundException(id));
        var updatedMeasurement = measurementMapper.partialUpdate(measurementDto, existingMeasurement);
        var savedMeasurement = measurementRepository.save(updatedMeasurement);
        return measurementMapper.toDto(savedMeasurement);
    }

    public void deleteMeasurement(Long id) {
        var measurement = measurementRepository.findById(id).orElseThrow(() -> new MeasurementNotFoundException(id));
        measurementRepository.delete(measurement);
    }
}
