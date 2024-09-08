package com.store_management_service.mapper;

import com.store_management_service.dto.CustomizableOptionDto;
import com.store_management_service.model.CustomizableOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MaterialOptionMapper.class })
public interface CustomizableOptionMapper {
    @Mapping(source = "materialDtos", target = "materials")
    CustomizableOption toEntity(CustomizableOptionDto customizableOptionDto);
    @Mapping(source = "materials", target = "materialDtos")
    CustomizableOptionDto toDto(CustomizableOption customizableOption);
    @Mapping(source = "materials", target = "materialDtos")
    List<CustomizableOptionDto> toDtos(List<CustomizableOption> customizableOptions);
}
