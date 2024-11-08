package com.user_management_service.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest (
        @NotBlank(message = "Username cannot be empty")
        String username,

        @NotBlank(message = "Password cannot be empty")
        String password
) {}