package com.store_management_service.mapper;

import com.store_management_service.dto.CustomizableMeasurementDto;
import com.store_management_service.model.CustomizableMeasurement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomizableMeasurementMapper {
    @Mapping(source = "modelDto", target = "model")
    @Mapping(source = "measurementDto", target = "measurement")
    CustomizableMeasurement toEntity(CustomizableMeasurementDto customizableMeasurementDto);

    @Mapping(source = "model", target = "modelDto")
    @Mapping(source = "measurement", target = "measurementDto")
    CustomizableMeasurementDto toDto(CustomizableMeasurement customizableMeasurement);

    @Mapping(source = "model", target = "modelDto")
    @Mapping(source = "measurement", target = "measurementDto")
    List<CustomizableMeasurementDto> toDtos(List<CustomizableMeasurement> customizableMeasurements);
}
