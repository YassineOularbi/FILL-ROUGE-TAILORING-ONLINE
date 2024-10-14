package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store_management_service.enums.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("product")
@Document(indexName = "products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    @Field(type = FieldType.Text)
    private String name;

    @Column(name = "description", nullable = false)
    @Field(type = FieldType.Text)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    @Field(type = FieldType.Keyword)
    private Category category;

    @Column(name = "picture", nullable = false)
    @Field(type = FieldType.Keyword)
    private String picture;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Field(type = FieldType.Keyword)
    private List<String> images;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_details", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "details_key")
    @Column(name = "details_value")
    @Field(type = FieldType.Object)
    private Map<String, String> details;

    @Column(name = "historical_story", nullable = false)
    @Field(type = FieldType.Text)
    private String historicalStory;

    @Column(name = "code_sku")
    @Field(type = FieldType.Keyword)
    private String codeSKU;

    @Column(name = "rating", columnDefinition = "Double default 0.00")
    @Field(type = FieldType.Double)
    private Double rating;

    @Column(name = "sales", columnDefinition = "Integer default 0")
    @Field(type = FieldType.Integer)
    private Integer sales;

    @Column(name = "authenticity_verified", columnDefinition = "Boolean default false")
    @Field(type = FieldType.Boolean)
    private Boolean authenticityVerified;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    @JsonIgnore
    private Store store;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "three_d_model_id")
    @JsonIgnore
    private ThreeDModel threeDModel;
}
