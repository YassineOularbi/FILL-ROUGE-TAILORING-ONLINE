package com.store_management_service.model;

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
    @JoinColumn(name = "material_id")
    private Material material;

    @ManyToOne
    @MapsId("optionId")
    @JoinColumn(name = "option_id")
    private CustomizableOption option;

}
