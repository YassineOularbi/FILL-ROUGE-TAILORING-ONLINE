package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    //
    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    @JsonIgnore
    private ThreeDModel model;

    @Enumerated(EnumType.STRING)
    //
    @Column(name = "type", nullable = false)
    private MaterialType type;

    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MaterialOption> materials;
}
