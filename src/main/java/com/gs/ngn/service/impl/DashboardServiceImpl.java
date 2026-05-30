package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.dto.response.*;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.AlertMapper;
import com.gs.ngn.mapper.HabitatMapper;
import com.gs.ngn.mapper.SpaceEventMapper;
import com.gs.ngn.repository.AlertRepository;
import com.gs.ngn.repository.HabitatRepository;
import com.gs.ngn.repository.ModuleRepository;
import com.gs.ngn.repository.ResourceMetricRepository;
import com.gs.ngn.repository.SpaceEventRepository;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.service.DashboardService;
import com.gs.ngn.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RF08 – Exibe dashboard em tempo real consolidando todos os dados do habitat.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final HabitatRepository       habitatRepository;
    private final AlertRepository         alertRepository;
    private final ModuleRepository        moduleRepository;
    private final ResourceMetricRepository metricRepository;
    private final SpaceEventRepository    spaceEventRepository;
    private final HabitatMapper           habitatMapper;
    private final AlertMapper             alertMapper;
    private final SpaceEventMapper        spaceEventMapper;

    @Override
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long habitatId) {
        log.debug("[DASHBOARD] Gerando dashboard para habitatId={}", habitatId);

        Habitat habitat = habitatRepository.findById(habitatId)
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", habitatId));

        var ac = habitat.getAtmosphericCondition();
        AtmosphericConditionResponse acResponse = ac == null ? null : new AtmosphericConditionResponse(
            ac.getOxygenLevel(), ac.getTemperature(), ac.getHumidity(),
            ac.getPressure(), ac.getRadiationLevel()
        );

        // Energia ativa consumida
        Double activeConsumption = moduleRepository.sumActiveEnergyConsumptionByHabitatId(habitatId);

        // Alertas abertos
        long openAlerts     = alertRepository.countByHabitatIdAndResolved(habitatId, false);
        long criticalAlerts = alertRepository
            .findByHabitatIdAndLevel(habitatId, AlertLevel.CRITICAL)
            .stream().filter(a -> !Boolean.TRUE.equals(a.getResolved())).count();

        // Últimos 5 alertas
        List<AlertResponse> recentAlerts = alertRepository
            .findByHabitatId(habitatId, PageRequest.of(0, 5, Sort.by("createdAt").descending()))
            .stream().map(alertMapper::toResponse).toList();

        // Médias de recursos
        ResourceMetricAverageResponse averages = metricRepository.calculateAverageByHabitatId(habitatId);

        // Eventos espaciais de alto risco
        List<SpaceEventResponse> highDangerEvents = spaceEventRepository
            .findByDangerLevelGreaterThanEqual(Constants.METEOR_DANGER_THRESHOLD)
            .stream().map(spaceEventMapper::toResponse).toList();

        return new DashboardResponse(
            LocalDateTime.now(),
            habitat.getId(), habitat.getName(), habitat.getPopulation(),
            acResponse,
            habitat.getAvailableEnergy(),
            activeConsumption != null ? activeConsumption : 0.0,
            habitat.getAvailableWater(),
            openAlerts, criticalAlerts,
            recentAlerts, averages, highDangerEvents
        );
    }
}
