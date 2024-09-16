package com.order_management_service.dto;

import com.order_management_service.enums.MaterialType;
import com.order_management_service.model.Material;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizedOptionDto {
    private MaterialType type;
    private Material material;
}
