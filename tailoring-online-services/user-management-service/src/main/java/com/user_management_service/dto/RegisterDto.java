package com.user_management_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    private String username;
    private String email;
    private String password;
    private String firstname;
    private String lastName;
}
