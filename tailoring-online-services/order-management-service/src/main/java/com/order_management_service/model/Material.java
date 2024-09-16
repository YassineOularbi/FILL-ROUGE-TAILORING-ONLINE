package com.order_management_service.model;

import com.order_management_service.enums.MaterialType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    private Long id;
    private String name;
    private String description;
    private String image;
    private MaterialType type;
    private Double unitPrice;
}
