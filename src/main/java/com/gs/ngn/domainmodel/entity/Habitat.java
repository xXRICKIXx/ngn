package com.gs.ngn.domainmodel.entity;

import com.gs.ngn.domainmodel.valueobject.AtmosphericCondition;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_habitat")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Habitat {

    @Id
    @Column(name = "habitat_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer population;

    @Column(nullable = false)
    private Double availableWater;

    @Column(nullable = false)
    private Double availableEnergy;

    @Embedded
    private AtmosphericCondition atmosphericCondition;

    @OneToMany(mappedBy = "habitat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Module> modules = new ArrayList<>();

    @OneToMany(mappedBy = "habitat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Alert> alerts = new ArrayList<>();

    @OneToMany(mappedBy = "habitat", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ResourceMetric> metrics = new ArrayList<>();
}
