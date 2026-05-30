package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.domainmodel.entity.Sensor;
import com.gs.ngn.domainmodel.enums.SensorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long>,  JpaSpecificationExecutor<Sensor> {
    List<Sensor> findByModuleId(Long moduleId);
    List<Sensor> findByType(SensorType type);
    List<Sensor> findByActiveTrue();
    List<Sensor> findByModuleHabitatIdAndType(Long habitatId, SensorType type);

}
