package com.store_management_service.mapper;

import com.store_management_service.dto.MaterialDto;
import com.store_management_service.model.Material;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MaterialMapper {
    Material toEntity(MaterialDto materialDto);
    MaterialDto toDto(Material material);
    Material partialUpdate(MaterialDto materialDto, @MappingTarget Material material);
}
