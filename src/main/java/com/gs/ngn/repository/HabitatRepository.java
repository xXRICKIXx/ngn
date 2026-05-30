package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.Habitat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HabitatRepository extends JpaRepository<Habitat, Long>, JpaSpecificationExecutor<Habitat> {
    Optional<Habitat> findByName(String name);
    boolean existsByName(String name);
}
