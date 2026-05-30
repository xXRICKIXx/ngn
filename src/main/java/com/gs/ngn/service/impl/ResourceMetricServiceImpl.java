package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.domainmodel.entity.ResourceMetric;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.dto.request.ResourceMetricRequest;
import com.gs.ngn.dto.response.ResourceMetricAverageResponse;
import com.gs.ngn.dto.response.ResourceMetricResponse;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.ResourceMetricMapper;
import com.gs.ngn.repository.HabitatRepository;
import com.gs.ngn.repository.ResourceMetricRepository;
import com.gs.ngn.service.AlertService;
import com.gs.ngn.service.ResourceMetricService;
import com.gs.ngn.util.Constants;
import com.gs.ngn.util.DateUtils;
import com.gs.ngn.validator.AtmosphereValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceMetricServiceImpl implements ResourceMetricService {

    private final ResourceMetricRepository resourceMetricRepository;
    private final HabitatRepository habitatRepository;
    private final AlertService alertService;
    private final ResourceMetricMapper resourceMetricMapper;
    private final AtmosphereValidator atmosphereValidator;

    @Override
    @Transactional
    public ResourceMetricResponse register(ResourceMetricRequest request) {
        Habitat habitat = habitatRepository.findById(request.habitatId())
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", request.habitatId()));

        ResourceMetric metric = ResourceMetric.builder()
            .oxygenLevel(request.oxygenLevel()).waterLevel(request.waterLevel())
            .pressure(request.pressure()).temperature(request.temperature())
            .humidity(request.humidity()).airQuality(request.airQuality())
            .foodProduction(request.foodProduction())
            .collectedAt(DateUtils.now()).habitat(habitat)
            .build();

        ResourceMetric saved = resourceMetricRepository.save(metric);
        log.debug("Métrica registrada para habitat id={}", habitat.getId());

        // RN02: analisa e dispara alertas automaticamente
        analyzeAndAlert(habitat.getId());

        return resourceMetricMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ResourceMetricResponse> findByHabitat(Long habitatId, Pageable pageable) {
        return resourceMetricRepository.findByHabitatId(habitatId, pageable).map(resourceMetricMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceMetricAverageResponse getAverageByHabitat(Long habitatId) {
        // RN09: calcular consumo médio de recursos
        habitatRepository.findById(habitatId)
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", habitatId));
        return resourceMetricRepository.calculateAverageByHabitatId(habitatId);
    }

    @Override
    @Transactional
    public void analyzeAndAlert(Long habitatId) {
        ResourceMetricAverageResponse avg = resourceMetricRepository.calculateAverageByHabitatId(habitatId);
        if (avg == null) return;

        // RN02: oxigênio abaixo do limite → alerta crítico
        if (avg.avgOxygenLevel() != null && atmosphereValidator.isOxygenCritical(avg.avgOxygenLevel())) {
            log.warn("[MONITOR] Oxigênio crítico ({}) no habitat id={}", avg.avgOxygenLevel(), habitatId);
            alertService.create(new AlertRequest(
                AlertType.OXYGEN, AlertLevel.CRITICAL,
                String.format("Nível médio de O₂ crítico: %.2f%%. Mínimo permitido: %.1f%%.",
                    avg.avgOxygenLevel(), Constants.MIN_OXYGEN_LEVEL),
                habitatId
            ));
        }

        // RN05: produção agrícola verifica disponibilidade hídrica
        if (avg.avgWaterLevel() != null && avg.avgWaterLevel() < Constants.MIN_WATER_FOR_AGRICULTURE_LITERS) {
            log.warn("[MONITOR] Água insuficiente para agricultura no habitat id={}", habitatId);
            alertService.create(new AlertRequest(
                AlertType.WATER, AlertLevel.HIGH,
                String.format("Reserva de água (%.2f L) abaixo do mínimo para produção agrícola (%.0f L).",
                    avg.avgWaterLevel(), Constants.MIN_WATER_FOR_AGRICULTURE_LITERS),
                habitatId
            ));
        }
    }
}
