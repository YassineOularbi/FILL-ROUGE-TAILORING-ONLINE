package com.store_management_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customizable_measurement")
public class CustomizableMeasurement {

    @EmbeddedId
    private CustomizableMeasurementKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("modelId")
    @JoinColumn(name = "model_id", nullable = false)
    private ThreeDModel model;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("measurementId")
    @JoinColumn(name = "measurement_id", nullable = false)
    private Measurement measurement;
}
