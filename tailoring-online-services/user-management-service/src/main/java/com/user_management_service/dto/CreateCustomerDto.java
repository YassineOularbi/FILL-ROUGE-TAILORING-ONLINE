package com.user_management_service.dto;

import com.user_management_service.validation.CreateGroup;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

public record CreateCustomerDto(
        @Validated(CreateGroup.class) CreateUserDto createUserDto
) implements Serializable {
}
