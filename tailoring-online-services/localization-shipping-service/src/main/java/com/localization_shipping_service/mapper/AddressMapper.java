package com.localization_shipping_service.mapper;

import com.localization_shipping_service.dto.AddressDto;
import com.localization_shipping_service.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
    Address partialUpdate(AddressDto addressDto, @MappingTarget Address address);
}
