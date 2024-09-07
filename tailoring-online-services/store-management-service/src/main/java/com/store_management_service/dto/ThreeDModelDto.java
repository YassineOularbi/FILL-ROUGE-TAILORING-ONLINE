package com.store_management_service.dto;

import com.store_management_service.model.CustomizableMeasurement;
import com.store_management_service.model.CustomizableOption;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ThreeDModelDto {
    private ProductDto productDto;
    private List<CustomizableMeasurement> measurements;
    private List<CustomizableOption> options;
}
