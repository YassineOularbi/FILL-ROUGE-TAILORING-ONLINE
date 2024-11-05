package com.store_management_service.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThreeDModelDto implements Serializable {
    private ProductDto productDto;
    private List<CustomizableMeasurementDto> measurementDtos;
    private List<CustomizableOptionDto> optionDtos;
}
