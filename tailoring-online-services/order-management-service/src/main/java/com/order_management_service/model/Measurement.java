package com.order_management_service.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Measurement implements Serializable {
    private Long id;
    private String name;
    private String description;
    private String logo;
}
