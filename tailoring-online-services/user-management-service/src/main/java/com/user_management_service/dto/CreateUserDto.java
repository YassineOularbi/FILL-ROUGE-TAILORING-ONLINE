package com.user_management_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user_management_service.enums.*;
import com.user_management_service.validation.CreateGroup;
import jakarta.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

public record CreateUserDto(
        @NotBlank(message = "Username cannot be empty", groups = {CreateGroup.class})
        @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters", groups = {CreateGroup.class})
        @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain alphanumeric characters and underscores", groups = {CreateGroup.class})
        String username,

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank(message = "Password cannot be empty", groups = {CreateGroup.class})
        @Size(min = 8, message = "Password must be at least 8 characters long", groups = {CreateGroup.class})
        @Pattern(regexp = ".*[A-Z].*", message = "Password must contain at least one uppercase letter", groups = {CreateGroup.class})
        @Pattern(regexp = ".*[a-z].*", message = "Password must contain at least one lowercase letter", groups = {CreateGroup.class})
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one digit", groups = {CreateGroup.class})
        @Pattern(regexp = ".*[!@#$%^&*].*", message = "Password must contain at least one special character", groups = {CreateGroup.class})
        String password,

        @NotBlank(message = "Email cannot be empty", groups = {CreateGroup.class})
        @Email(message = "Invalid email format", groups = {CreateGroup.class})
        String email,

        @NotBlank(message = "First name cannot be empty", groups = {CreateGroup.class})
        @Size(max = 50, message = "First name cannot exceed 50 characters", groups = {CreateGroup.class})
        @Pattern(regexp = "^[a-zA-Z]+$", message = "First name can only contain letters", groups = {CreateGroup.class})
        String firstName,

        @NotBlank(message = "Last name cannot be empty", groups = {CreateGroup.class})
        @Size(max = 50, message = "Last name cannot exceed 50 characters", groups = {CreateGroup.class})
        @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name can only contain letters", groups = {CreateGroup.class})
        String lastName,

        @Pattern(regexp = "^(\\+\\d{1,2}\\s?)?\\(?\\d{1,4}\\)?[\\s\\-]?\\d{1,4}[\\s\\-]?\\d{1,4}[\\s\\-]?\\d{1,9}$", message = "Invalid phone number format", groups = {CreateGroup.class})
        String phoneNumber,

        @NotNull(message = "Date of birth cannot be null", groups = {CreateGroup.class})
        @Past(message = "Date of birth must be in the past", groups = {CreateGroup.class})
        LocalDate dateOfBirth,

        @NotNull(message = "Language preference cannot be null", groups = {CreateGroup.class})
        LanguagePreference languagePreference,

        @NotNull(message = "Gender cannot be null", groups = {CreateGroup.class})
        Gender gender
) implements Serializable {

}
