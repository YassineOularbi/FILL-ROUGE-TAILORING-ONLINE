package com.order_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order_management_service.enums.MaterialType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customized_option")
public class CustomizedOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "type", nullable = false)
    private MaterialType type;

    @Column(name = "material_id", nullable = false)
    private Long materialId;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private CustomizedProduct product;
}
