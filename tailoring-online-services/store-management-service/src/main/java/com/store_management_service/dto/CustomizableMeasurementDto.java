package com.store_management_service.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizableMeasurementDto {
    public ThreeDModelDto model;
    public MeasurementDto measurement;
}
