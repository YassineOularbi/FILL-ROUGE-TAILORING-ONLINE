package com.user_management_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.user_management_service.validation.CreateGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;


public record AuthenticationRequest (
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
        String password
) {}