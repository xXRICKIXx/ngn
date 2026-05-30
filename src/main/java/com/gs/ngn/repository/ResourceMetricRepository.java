package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.ResourceMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResourceMetricRepository extends JpaRepository<ResourceMetric, Long> {
    Page<ResourceMetric> findByHabitatId(Long habitatId, Pageable pageable);
    List<ResourceMetric> findByHabitatIdAndCollectedAtBetween(Long habitatId, LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT new com.gs.ngn.dto.response.ResourceMetricAverageResponse(
            h.id, h.name,
            AVG(rm.oxygenLevel), AVG(rm.waterLevel), AVG(rm.pressure),
            AVG(rm.temperature), AVG(rm.humidity), AVG(rm.airQuality), AVG(rm.foodProduction)
        )
        FROM ResourceMetric rm JOIN rm.habitat h
        WHERE h.id = :habitatId
        GROUP BY h.id, h.name
        """)
    com.gs.ngn.dto.response.ResourceMetricAverageResponse calculateAverageByHabitatId(Long habitatId);
}
