package com.user_management_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        @NotNull(message = "Timestamp cannot be null")
        LocalDateTime timestamp,

        @Positive(message = "Status must be positive")
        int status,

        @NotBlank(message = "Error cannot be blank")
        @Size(min = 1, max = 500, message = "Error length must be between 1 and 500 characters")
        String error,

        @NotBlank(message = "Message cannot be blank")
        @Size(min = 1, max = 500, message = "Message length must be between 1 and 500 characters")
        String message,

        @NotBlank(message = "Path cannot be blank")
        @Size(min = 1, max = 500, message = "Path length must be between 1 and 500 characters")
        String path,

        @Valid
        @NotEmpty(message = "Details cannot be empty")
        @Size(min = 1, max = 500, message = "Each detail length must be between 1 and 500 characters")
        List<@NotBlank(message = "Detail cannot be blank") String> details
) {
}
