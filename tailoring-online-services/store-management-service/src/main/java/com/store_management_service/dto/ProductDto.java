package com.store_management_service.dto;

import com.store_management_service.enums.Category;
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
public class ProductDto implements Serializable {
    private String name;
    private String description;
    private Category category;
    private String picture;
    private List<String> images;
    private Map<String, String> details;
    private String historicalStory;
    private String codeSKU;
    private Double rating;
    private Integer sales;
    private Boolean authenticityVerified;
}
