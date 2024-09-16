package com.order_management_service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    private Long id;
    private String name;
    private String description;
    private String logo;
}
