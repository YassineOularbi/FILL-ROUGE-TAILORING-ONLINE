package com.product_management_service.model;

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
public class Store {
    private Long id;
    private String name;
    private String description;
    private Map<String, String> type;
    private List<String> images;
    private String logo;
    private String coverImage;
    private Double rating;
    private String tailorId;
}
