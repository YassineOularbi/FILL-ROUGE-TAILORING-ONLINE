package com.store_management_service.mapper;

import com.store_management_service.dto.ThreeDModelDto;
import com.store_management_service.model.ThreeDModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ThreeDModelMapper {
    ThreeDModel toEntity(ThreeDModelDto threeDModelDto);
    ThreeDModelDto toDto (ThreeDModel threeDModel);
}
