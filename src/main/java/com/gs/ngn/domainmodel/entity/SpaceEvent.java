package com.gs.ngn.domainmodel.entity;

import com.gs.ngn.domainmodel.enums.SpaceEventType;
import com.gs.ngn.domainmodel.valueobject.Coordinates;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_space_event")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class SpaceEvent {

    @Id
    @Column(name = "space_event_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceEventType type;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Double dangerLevel;

    @Column(nullable = false)
    private LocalDateTime eventDate;

    @Embedded
    private Coordinates coordinates;
}
