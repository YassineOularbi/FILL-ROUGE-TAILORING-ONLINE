package com.store_management_service.model;

import com.store_management_service.enums.MaterialType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "three_d_model")
public class ThreeDModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToMany
    @JoinTable(
            name = "customizable_measurements",
            joinColumns = @JoinColumn(name = "three_d_model_id"),
            inverseJoinColumns = @JoinColumn(name = "measurement_id")
    )
    private List<Measurement> measurements;

    @OneToMany
    private List<CustomizableOption> options;
}
