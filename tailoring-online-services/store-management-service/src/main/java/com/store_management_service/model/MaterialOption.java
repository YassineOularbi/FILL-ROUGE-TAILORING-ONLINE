package com.store_management_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material_option")
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeAlias("")
@Document(indexName = "material_options")
public class MaterialOption implements Serializable {

    @EmbeddedId
    private MaterialOptionKey id;

    @ManyToOne
    @MapsId("materialId")
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

    @ManyToOne
    @MapsId("optionId")
    @JoinColumn(name = "option_id", nullable = false)
    @JsonIgnore
    private CustomizableOption option;
}
