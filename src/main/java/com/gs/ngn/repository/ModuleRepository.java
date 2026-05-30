package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.Module;
import com.gs.ngn.domainmodel.enums.ModuleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
    List<Module> findByHabitatId(Long habitatId);
    List<Module> findByHabitatIdAndActive(Long habitatId, boolean active);
    List<Module> findByType(ModuleType type);

    @Query("SELECT SUM(m.energyConsumption) FROM Module m WHERE m.habitat.id = :habitatId AND m.active = true")
    Double sumActiveEnergyConsumptionByHabitatId(Long habitatId);
}
