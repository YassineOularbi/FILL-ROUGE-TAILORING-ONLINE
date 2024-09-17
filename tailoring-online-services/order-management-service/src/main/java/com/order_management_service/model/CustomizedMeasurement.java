package com.order_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.order_management_service.enums.MeasurementUnit;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customized_measurement")
public class CustomizedMeasurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "measurement_id", nullable = false)
    private Long measurementId;

    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "unit", nullable = false)
    private MeasurementUnit unit;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private CustomizedProduct product;
}
