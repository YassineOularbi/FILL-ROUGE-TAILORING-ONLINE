package com.store_management_service.mapper;

import com.store_management_service.dto.MeasurementDto;
import com.store_management_service.model.Measurement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MeasurementMapper {
    Measurement toEntity(MeasurementDto measurementDto);
    MeasurementDto toDto(Measurement measurement);
    Measurement partialUpdate(MeasurementDto measurementDto, @MappingTarget Measurement measurement);
}
