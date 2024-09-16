package com.store_management_service.mapper;

import com.store_management_service.dto.ThreeDModelDto;
import com.store_management_service.model.ThreeDModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { ProductMapper.class, CustomizableMeasurementMapper.class, CustomizableOptionMapper.class })
public interface ThreeDModelMapper {
    @Mapping(source = "productDto", target = "product")
    @Mapping(source = "measurementDtos", target = "measurements")
    @Mapping(source = "optionDtos", target = "options")
    ThreeDModel toEntity(ThreeDModelDto threeDModelDto);
    @Mapping(source = "product", target = "productDto")
    @Mapping(source = "measurements", target = "measurementDtos")
    @Mapping(source = "options", target = "optionDtos")
    ThreeDModelDto toDto (ThreeDModel threeDModel);

    @Mapping(source = "product", target = "productDto")
    @Mapping(source = "measurements", target = "measurementDtos")
    @Mapping(source = "options", target = "optionDtos")
    List<ThreeDModelDto> toDtos (List<ThreeDModel> threeDModels);
}
