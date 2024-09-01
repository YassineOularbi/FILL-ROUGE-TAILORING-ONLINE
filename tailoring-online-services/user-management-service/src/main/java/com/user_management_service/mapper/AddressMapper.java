package com.user_management_service.mapper;

import com.user_management_service.dto.AddressDto;
import com.user_management_service.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address toEntity(AddressDto addressDto);
    AddressDto toDto(Address address);
    List<Address> toEntities(List<AddressDto> addressDtoList);
    List<AddressDto> toDtoList(List<Address> addresses);
    Address partialUpdate(AddressDto addressDto, @MappingTarget Address address);
}
