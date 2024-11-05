package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("")
@Document(indexName = "stores")
public class Store implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    @Field(type = FieldType.Keyword)
    private String name;

    @Column(name = "description", nullable = false)
    @Field(type = FieldType.Keyword)
    private String description;

    @ElementCollection
    @CollectionTable(name = "store_types", joinColumns = @JoinColumn(name = "store_id"))
    @MapKeyColumn(name = "type_key")
    @Column(name = "type_value")
    private Map<String, String> type;

    @ElementCollection
    @CollectionTable(name = "store_images", joinColumns = @JoinColumn(name = "store_id"))
    @Column(name = "image_url")
    private List<String> images;

    @Column(name = "logo", nullable = false)
    private String logo;

    @Column(name = "cover_image", nullable = false)
    private String coverImage;

    @Column(name = "rating", nullable = false, columnDefinition = "Double default 0.00")
    private Double rating;

    @Column(name = "tailor_id", nullable = false)
    private String tailorId;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

}
