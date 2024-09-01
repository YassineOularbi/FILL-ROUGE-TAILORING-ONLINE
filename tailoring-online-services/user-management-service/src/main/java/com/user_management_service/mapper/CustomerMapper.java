package com.user_management_service.mapper;

import com.user_management_service.dto.CustomerDto;
import com.user_management_service.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    Customer toEntity(CustomerDto customerDto);
    CustomerDto toDto(Customer customer);
    List<Customer> toEntities(List<CustomerDto> customerDtoList);
    List<CustomerDto> toDtoList(List<Customer> customers);
    Customer partialUpdate(CustomerDto customerDto, @MappingTarget Customer customer);
}
