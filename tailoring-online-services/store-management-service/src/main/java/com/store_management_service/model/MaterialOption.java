package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material_option")
public class MaterialOption {

    @EmbeddedId
    private MaterialOptionKey id;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @MapsId("optionId")
    @JoinColumn(name = "option_id", nullable = false)
    @JsonIgnore
    private CustomizableOption option;
}
