package com.order_management_service.mapper;

import com.order_management_service.dto.CustomizedProductDto;
import com.order_management_service.model.CustomizedProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { CustomizedMeasurementMapper.class, CustomizedOptionMapper.class })
public interface CustomizedProductMapper {
    @Mapping(target = "measurements", source = "measurementDtos")
    @Mapping(target = "options", source = "optionDtos")
    CustomizedProduct toEntity(CustomizedProductDto customizedProductDto);
    @Mapping(target = "measurementDtos", source = "measurements")
    @Mapping(target = "optionDtos", source = "options")
    CustomizedProductDto toDto(CustomizedProduct customizedProduct);

    @Mapping(target = "measurements", source = "measurementDtos")
    @Mapping(target = "options", source = "optionDtos")
    CustomizedProduct partialUpdate(CustomizedProductDto customizedProductDto, @MappingTarget CustomizedProduct customizedProduct);
    @Mapping(target = "measurements", source = "measurementDtos")
    @Mapping(target = "options", source = "optionDtos")
    List<CustomizedProduct> toEntities(List<CustomizedProductDto> customizedProductDtos);
    @Mapping(target = "measurementDtos", source = "measurements")
    @Mapping(target = "optionDtos", source = "options")
    List<CustomizedProductDto> toDtos(List<CustomizedProduct> customizedProducts);
}
