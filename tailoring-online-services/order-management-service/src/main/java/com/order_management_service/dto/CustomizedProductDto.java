package com.order_management_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedProductDto {
    private Long productId;
    private List<CustomizedMeasurementDto> measurementDtos;
    private List<CustomizedOptionDto> optionDtos;
}
