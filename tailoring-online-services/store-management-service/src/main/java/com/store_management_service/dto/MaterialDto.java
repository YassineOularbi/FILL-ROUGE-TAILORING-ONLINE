package com.store_management_service.dto;

import com.store_management_service.enums.MaterialType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialDto implements Serializable {
    private String name;
    private String description;
    private String image;
    private MaterialType type;
    private Double unitPrice;
}
