package com.gs.ngn.domainmodel.entity;

import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_alert")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlertLevel level;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean resolved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitat_id", nullable = false)
    private Habitat habitat;

    @OneToMany(mappedBy = "alert", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AIAction> aiActions = new ArrayList<>();
}
