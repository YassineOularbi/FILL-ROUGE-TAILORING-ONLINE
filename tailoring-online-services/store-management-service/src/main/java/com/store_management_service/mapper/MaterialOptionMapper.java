package com.store_management_service.mapper;

import com.store_management_service.dto.MaterialOptionDto;
import com.store_management_service.model.MaterialOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { MaterialMapper.class })
public interface MaterialOptionMapper {
    @Mapping(source = "materialDto", target = "material")
    MaterialOption toEntity(MaterialOptionDto materialOptionDto);
    @Mapping(source = "material", target = "materialDto")
    MaterialOptionDto toDto(MaterialOption materialOption);
    @Mapping(source = "material", target = "materialDto")
    List<MaterialOptionDto> toDtos(List<MaterialOption> materialOptions);
}
