package com.user_management_service.mapper;

import com.user_management_service.dto.CreateCustomerDto;
import com.user_management_service.model.Customer;
import com.user_management_service.validation.CreateGroup;
import jakarta.validation.*;
import org.mapstruct.*;

import java.util.Set;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {

    @Mappings({
            @Mapping(target = "username", source = "createCustomerDto.createUserDto.username"),
            @Mapping(target = "password", source = "createCustomerDto.createUserDto.password"),
            @Mapping(target = "email", source = "createCustomerDto.createUserDto.email"),
            @Mapping(target = "firstName", source = "createCustomerDto.createUserDto.firstName"),
            @Mapping(target = "lastName", source = "createCustomerDto.createUserDto.lastName"),
            @Mapping(target = "phoneNumber", source = "createCustomerDto.createUserDto.phoneNumber"),
            @Mapping(target = "dateOfBirth", source = "createCustomerDto.createUserDto.dateOfBirth"),
            @Mapping(target = "languagePreference", source = "createCustomerDto.createUserDto.languagePreference"),
            @Mapping(target = "gender", source = "createCustomerDto.createUserDto.gender")
    })
    Customer toCreateEntity(CreateCustomerDto createCustomerDto);

    @AfterMapping
    default void validateAfterMapping(@MappingTarget Customer customer) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer, CreateGroup.class);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
