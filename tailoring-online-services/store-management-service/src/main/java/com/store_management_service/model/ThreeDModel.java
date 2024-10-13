package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "three_d_model")
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("")
@Document(indexName = "three_d_models")
public class ThreeDModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomizableMeasurement> measurements;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomizableOption> options;
}
