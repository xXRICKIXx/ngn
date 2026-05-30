package com.gs.ngn.domainmodel.entity;

import com.gs.ngn.domainmodel.enums.ModuleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_module")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class Module {

    @Id
    @Column(name = "module_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModuleType type;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private Double energyConsumption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "habitat_id", nullable = false)
    private Habitat habitat;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Sensor> sensors = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "tb_module_crew",
        joinColumns = @JoinColumn(name = "module_id"),
        inverseJoinColumns = @JoinColumn(name = "crew_member_id")
    )
    @Builder.Default
    private List<CrewMember> crewMembers = new ArrayList<>();
}
