package com.store_management_service.mapper;

import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.model.CustomizableMeasurement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomizableMeasurementMapper {
    CustomizableMeasurement toEntity(CustomizableMeasurementDto customizableMeasurementDto);
    CustomizableMeasurementDto toDto(CustomizableMeasurement customizableMeasurement);
}
