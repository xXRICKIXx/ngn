package com.gs.ngn.domainmodel.entity;

import com.gs.ngn.domainmodel.enums.AIActionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_ai_action")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class AIAction {

    @Id
    @Column(name = "ai_action_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AIActionType type;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean executed;

    private LocalDateTime executedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alert_id", nullable = false)
    private Alert alert;
}
