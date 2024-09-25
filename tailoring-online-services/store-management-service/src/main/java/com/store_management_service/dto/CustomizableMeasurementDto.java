package com.store_management_service.dto;


import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizableMeasurementDto implements Serializable {
    private MeasurementDto measurementDto;
}
