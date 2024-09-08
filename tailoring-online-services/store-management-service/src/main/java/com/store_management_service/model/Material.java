package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.store_management_service.enums.MaterialType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material")
@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
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
