package com.order_management_service.dto;

import com.order_management_service.model.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedProductDto {
    private Product product;
    private List<CustomizedMeasurementDto> measurementDtos;
    private List<CustomizedOptionDto> optionDtos;
}
