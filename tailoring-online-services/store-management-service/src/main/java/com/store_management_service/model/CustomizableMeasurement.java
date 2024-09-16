package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customizable_measurement")
public class CustomizableMeasurement implements Serializable {

    @EmbeddedId
    private CustomizableMeasurementKey id;

    @ManyToOne
    @MapsId("modelId")
    @JoinColumn(name = "model_id", nullable = false)
    @JsonIgnore
    private ThreeDModel model;

    @ManyToOne
    @MapsId("measurementId")
    @JoinColumn(name = "measurement_id", nullable = false)
    private Measurement measurement;
}
