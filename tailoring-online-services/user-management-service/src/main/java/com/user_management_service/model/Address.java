package com.user_management_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "suite")
    private String suite;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "zip_code", nullable = false)
    private String zipCode;

    @Column(name = "is_default", nullable = false, columnDefinition = "false")
    private Boolean isDefault;

    @OneToMany
    @JoinColumn(name = "bank_id", nullable = false)
    private List<Bank> banks;
}
