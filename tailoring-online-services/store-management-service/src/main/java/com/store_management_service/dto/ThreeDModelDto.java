package com.store_management_service.dto;

import com.store_management_service.model.CustomizableOption;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThreeDModelDto {
    private ProductDto product;
    private List<CustomizableMeasurementDto> measurements;
    private List<CustomizableOption> options;
}
