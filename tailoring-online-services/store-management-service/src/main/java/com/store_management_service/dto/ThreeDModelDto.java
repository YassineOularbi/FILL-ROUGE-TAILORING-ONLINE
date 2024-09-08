package com.store_management_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThreeDModelDto {
    private ProductDto productDto;
    private List<CustomizableMeasurementDto> measurementDtos;
    private List<CustomizableOptionDto> optionDtos;
}
