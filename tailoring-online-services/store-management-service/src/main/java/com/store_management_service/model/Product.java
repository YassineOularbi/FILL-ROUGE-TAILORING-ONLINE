package com.store_management_service.model;

import com.store_management_service.enums.Category;
import com.store_management_service.enums.MaterialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "picture", nullable = false)
    private String picture;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @ElementCollection
    @CollectionTable(name = "product_details", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "details_key")
    @Column(name = "details_value")
    private Map<String, String> details;

    @Column(name = "historical_story", nullable = false)
    private String historicalStory;

    @Column(name = "code_sku")
    private String codeSKU;

    @Column(name = "rating", columnDefinition = "Double default 0.00")
    private Double rating;

    @Column(name = "sales", columnDefinition = "Integer default 0")
    private Integer sales;

    @Column(name = "authenticity_verified", columnDefinition = "Boolean default false")
    private Boolean authenticityVerified;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
