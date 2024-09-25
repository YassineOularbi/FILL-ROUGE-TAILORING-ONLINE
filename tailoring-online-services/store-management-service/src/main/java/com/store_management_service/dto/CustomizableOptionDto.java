package com.store_management_service.dto;

import com.store_management_service.enums.MaterialType;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizableOptionDto implements Serializable {
    private MaterialType type;
    private List<MaterialOptionDto> materialDtos;
}
