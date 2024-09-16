package com.store_management_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaterialOptionKey implements Serializable {

    @Column(name = "material_id")
    private Long materialId;

    @Column(name = "option_id")
    private Long optionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialOptionKey that = (MaterialOptionKey) o;
        return Objects.equals(materialId, that.materialId) && Objects.equals(optionId, that.optionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materialId, optionId);
    }
}
