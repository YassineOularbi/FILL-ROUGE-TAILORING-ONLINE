package com.store_management_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomizableMeasurementKey implements Serializable {

    @Column(name = "model_id")
    private Long modelId;

    @Column(name = "measurement_id")
    private Long measurementId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomizableMeasurementKey that = (CustomizableMeasurementKey) o;
        return Objects.equals(modelId, that.modelId) && Objects.equals(measurementId, that.measurementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelId, measurementId);
    }
}
