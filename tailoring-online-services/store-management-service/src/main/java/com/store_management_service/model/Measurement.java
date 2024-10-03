package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "measurement")
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("")
@Document(indexName = "measurements")
public class Measurement implements Serializable {

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

    @Column(name = "logo", nullable = false)
    private String logo;

    @OneToMany(mappedBy = "measurement", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<CustomizableMeasurement> measurements;
}
