package com.store_management_service.mapper;

import com.store_management_service.dto.StoreDto;
import com.store_management_service.model.Store;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StoreMapper {
    Store toEntity(StoreDto storeDto);
    StoreDto toDto(Store store);
    Store partialUpdate(StoreDto storeDto, @MappingTarget Store store);
}
