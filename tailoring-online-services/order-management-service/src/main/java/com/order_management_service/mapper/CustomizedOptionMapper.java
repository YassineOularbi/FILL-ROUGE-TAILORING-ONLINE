package com.order_management_service.mapper;

import com.order_management_service.dto.CustomizedOptionDto;
import com.order_management_service.model.CustomizedOption;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomizedOptionMapper {
    CustomizedOption toEntity(CustomizedOptionDto customizedOptionDto);
    CustomizedOptionDto toDto(CustomizedOption customizedOption);
    CustomizedOption partialUpdate(CustomizedOptionDto customizedOptionDto, @MappingTarget CustomizedOption customizedOption);
    List<CustomizedOption> toEntities(List<CustomizedOptionDto> customizedOptionDtos);
    List<CustomizedOptionDto> toDtos(List<CustomizedOption> customizedOptions);
}
