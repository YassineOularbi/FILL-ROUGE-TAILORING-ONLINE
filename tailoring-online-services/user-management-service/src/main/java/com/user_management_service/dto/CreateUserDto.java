package com.user_management_service.dto;

import com.user_management_service.enums.*;
import com.user_management_service.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;

public record CreateUserDto(
        @NotBlank(message = "Username cannot be empty", groups = {CreateGroup.class})
        String username,

        @NotBlank(message = "Password cannot be empty", groups = {CreateGroup.class})
        String password,

        @NotBlank(message = "Email cannot be empty", groups = {CreateGroup.class})
        String email,

        @NotBlank(message = "First name cannot be empty", groups = {CreateGroup.class})
        String firstName,

        @NotBlank(message = "Last name cannot be empty", groups = {CreateGroup.class})
        String lastName,

        @NotBlank(message = "Phone number cannot be empty", groups = {CreateGroup.class})
        String phoneNumber,

        @NotNull(message = "Date of birth cannot be null", groups = {CreateGroup.class})
        LocalDate dateOfBirth,

        @NotNull(message = "Language preference cannot be null", groups = {CreateGroup.class})
        LanguagePreference languagePreference,

        @NotNull(message = "Gender cannot be null", groups = {CreateGroup.class})
        Gender gender
) implements Serializable {

}
