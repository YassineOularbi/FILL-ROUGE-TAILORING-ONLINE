package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.store_management_service.enums.MaterialType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material")
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("")
@Document(indexName = "materials")
public class Material implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    @Field(type = FieldType.Keyword)
    private String name;

    @Column(name = "description", nullable = false)
    @Field(type = FieldType.Keyword)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Field(type = FieldType.Keyword)
    private MaterialType type;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<MaterialOption> materials;
}
