package com.gs.ngn.domainmodel.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_crew_member")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode(of = "id")
public class CrewMember {

    @Id
    @Column(name = "crew_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private Integer experienceLevel;

    @ManyToMany(mappedBy = "crewMembers")
    @Builder.Default
    private List<Module> modules = new ArrayList<>();
}
