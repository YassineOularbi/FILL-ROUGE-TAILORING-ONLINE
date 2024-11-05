package com.order_management_service.mapper;

import com.order_management_service.dto.CustomizedMeasurementDto;
import com.order_management_service.model.CustomizedMeasurement;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomizedMeasurementMapper {
    CustomizedMeasurement toEntity(CustomizedMeasurementDto customizedMeasurementDto);
    CustomizedMeasurementDto toDto(CustomizedMeasurement customizedMeasurement);
    CustomizedMeasurement partialUpdate(CustomizedMeasurementDto customizedMeasurementDto, @MappingTarget CustomizedMeasurement customizedMeasurement);
    List<CustomizedMeasurement> toEntities(List<CustomizedMeasurementDto> customizedMeasurementDtos);
    List<CustomizedMeasurementDto> toDtos(List<CustomizedMeasurement> customizedMeasurements);
}
