package com.user_management_service.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TailorDto extends UserDto implements Serializable {
    private String bio;
    private String specialty;
    private Double rating;
}
