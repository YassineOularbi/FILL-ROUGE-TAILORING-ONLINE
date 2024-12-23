package com.order_management_service.dto;

import com.order_management_service.enums.MeasurementUnit;
import com.order_management_service.model.Measurement;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedMeasurementDto implements Serializable {
    private Measurement measurement;
    private Double value;
    private MeasurementUnit unit;
}
