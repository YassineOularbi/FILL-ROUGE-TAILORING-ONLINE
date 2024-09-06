package com.store_management_service.model;

import com.store_management_service.enums.MaterialType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customizable_option")
public class CustomizableOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "three_d_model_id", nullable = false)
    private ThreeDModel threeDModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "material_type", nullable = false)
    private MaterialType materialType;

    @OneToMany(mappedBy = "customizable_option")
    @JoinTable(
            name = "material_list",
            joinColumns = @JoinColumn(name = "material_option_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id")
    )
    private List<MaterialOption> materials;
}
