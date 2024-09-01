package com.user_management_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TailorDto extends UserDto {

    private String bio;
    private String specialty;
    private Double rating;
    private List<BankDto> banks;
}
