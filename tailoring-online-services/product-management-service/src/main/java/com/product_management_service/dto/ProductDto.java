package com.product_management_service.dto;

import com.product_management_service.enums.Category;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

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
}
