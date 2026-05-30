package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.SpaceEvent;
import com.gs.ngn.domainmodel.enums.SpaceEventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SpaceEventRepository extends JpaRepository<SpaceEvent, Long>, JpaSpecificationExecutor<SpaceEvent> {
    Page<SpaceEvent> findByType(SpaceEventType type, Pageable pageable);
    List<SpaceEvent> findByDangerLevelGreaterThanEqual(Double dangerLevel);
    List<SpaceEvent> findByEventDateBetween(LocalDateTime start, LocalDateTime end);
}
