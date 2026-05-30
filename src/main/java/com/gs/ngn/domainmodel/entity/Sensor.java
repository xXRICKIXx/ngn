package com.gs.ngn.domainmodel.entity;

import com.gs.ngn.domainmodel.enums.SensorType;
import com.gs.ngn.domainmodel.valueobject.Coordinates;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_sensor")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Sensor {

    @Id
    @Column(name = "sensor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SensorType type;

    @Column(nullable = false)
    private Double currentValue;

    @Column(nullable = false)
    private Boolean active;

    @Embedded
    private Coordinates coordinates;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id", nullable = false)
    private Module module;
}
