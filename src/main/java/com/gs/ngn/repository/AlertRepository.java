package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {
    List<Alert> findByHabitatIdAndResolved(Long habitatId, Boolean resolved);
    List<Alert> findByHabitatIdAndLevel(Long habitatId, AlertLevel level);
    Page<Alert> findByHabitatId(Long habitatId, Pageable pageable);
    List<Alert> findByTypeAndResolved(AlertType type, Boolean resolved);
    long countByHabitatIdAndResolved(Long habitatId, Boolean resolved);
}
