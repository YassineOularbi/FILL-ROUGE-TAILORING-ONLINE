package com.order_management_service.model;

import com.order_management_service.enums.MaterialType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Material implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String image;
    private MaterialType type;
    private Double unitPrice;
}
