package com.order_management_service.model;

import com.order_management_service.enums.Category;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {
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
}
