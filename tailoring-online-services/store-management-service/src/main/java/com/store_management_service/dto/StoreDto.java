package com.store_management_service.dto;

import com.store_management_service.model.Tailor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreDto implements Serializable {
    private String name;
    private String description;
    private Map<String, String> type;
    private List<String> images;
    private String logo;
    private String coverImage;
    private Double rating;
    private Tailor tailor;
}
