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

    @ManyToOne
    @MapsId("modelId")
    @JoinColumn(name = "model_id")
    private ThreeDModel model;

    @ManyToOne
    @MapsId("measurementId")
    @JoinColumn(name = "measurement_id")
    private Measurement measurement;
}
