package com.localization_shipping_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "address")
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("")
@Document(indexName = "addresses")
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "address", nullable = false)
    @Field(type = FieldType.Keyword)
    private String address;

    @Column(name = "suite")
    @Field(type = FieldType.Keyword)
    private String suite;

    @Column(name = "city", nullable = false)
    @Field(type = FieldType.Keyword)
    private String city;

    @Column(name = "province", nullable = false)
    @Field(type = FieldType.Keyword)
    private String province;

    @Column(name = "country", nullable = false)
    @Field(type = FieldType.Keyword)
    private String country;

    @Column(name = "zip_code", nullable = false)
    @Field(type = FieldType.Keyword)
    private String zipCode;

    @Column(name = "is_default", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDefault;

    @Column(name = "user_id", nullable = false)
    private String userId;

}
