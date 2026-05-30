package com.gs.ngn.domainmodel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_resource_metric")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class ResourceMetric {

    @Id
    @Column(name = "metric_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double oxygenLevel;

    @Column(nullable = false)
    private Double waterLevel;

    @Column(nullable = false)
    private Double pressure;

    @Column(nullable = false)
    private Double temperature;

    @Column(nullable = false)
    private Double humidity;

    @Column(nullable = false)
    private Double airQuality;

    @Column(nullable = false)
    private Double foodProduction;

    @Column(nullable = false)
    private LocalDateTime collectedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitat_id", nullable = false)
    private Habitat habitat;
}
