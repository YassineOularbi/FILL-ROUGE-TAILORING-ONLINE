package com.store_management_service.dto;

import com.store_management_service.enums.Category;
import com.store_management_service.model.Store;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    private Long id;
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
    private Store store;
}
