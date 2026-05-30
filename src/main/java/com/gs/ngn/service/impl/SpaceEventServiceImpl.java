package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.SpaceEvent;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.domainmodel.enums.SpaceEventType;
import com.gs.ngn.domainmodel.valueobject.Coordinates;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.dto.request.SpaceEventRequest;
import com.gs.ngn.dto.response.SpaceEventResponse;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.SpaceEventMapper;
import com.gs.ngn.repository.HabitatRepository;
import com.gs.ngn.repository.SpaceEventRepository;
import com.gs.ngn.service.AlertService;
import com.gs.ngn.service.SpaceEventService;
import com.gs.ngn.specification.SpaceEventSpecification;
import com.gs.ngn.util.Constants;
import com.gs.ngn.validator.SpaceEventValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceEventServiceImpl implements SpaceEventService {

    private final SpaceEventRepository spaceEventRepository;
    private final HabitatRepository    habitatRepository;
    private final AlertService         alertService;
    private final SpaceEventMapper     spaceEventMapper;
    private final SpaceEventValidator  spaceEventValidator;

    @Override
    @Transactional
    public SpaceEventResponse create(SpaceEventRequest request) {
        spaceEventValidator.validate(request);

        SpaceEvent event = SpaceEvent.builder()
            .type(request.type()).description(request.description())
            .dangerLevel(request.dangerLevel()).eventDate(request.eventDate())
            .coordinates(new Coordinates(request.latitude(), request.longitude()))
            .build();

        SpaceEvent saved = spaceEventRepository.save(event);
        log.info("SpaceEvent id={} tipo={} dangerLevel={} registrado.", saved.getId(), saved.getType(), saved.getDangerLevel());
        return spaceEventMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public SpaceEventResponse findById(Long id) {
        return spaceEventMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpaceEventResponse> findAll(Pageable pageable) {
        return spaceEventRepository.findAll(pageable).map(spaceEventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SpaceEventResponse> findFiltered(SpaceEventType type, Double minDanger,
                                                  LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Specification<SpaceEvent> spec = SpaceEventSpecification.hasType(type)
            .and(SpaceEventSpecification.hasDangerLevelAbove(minDanger))
            .and(SpaceEventSpecification.occurredAfter(from))
            .and(SpaceEventSpecification.occurredBefore(to));
        return spaceEventRepository.findAll(spec, pageable).map(spaceEventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SpaceEventResponse> findHighDangerEvents() {
        return spaceEventRepository
            .findByDangerLevelGreaterThanEqual(Constants.METEOR_DANGER_THRESHOLD)
            .stream().map(spaceEventMapper::toResponse).toList();
    }

    /**
     * RN03 – Processa evento espacial perigoso: valida o limiar de perigo e emite
     * alerta automático no habitat alvo, que por sua vez aciona a strategy correspondente.
     */
    @Override
    @Transactional
    public void processDangerousEvent(Long eventId, Long habitatId) {
        SpaceEvent event = getOrThrow(eventId);
        habitatRepository.findById(habitatId)
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", habitatId));

        if (!spaceEventValidator.isHighDanger(event.getDangerLevel())) {
            log.info("Evento id={} dangerLevel={} abaixo do limiar ({}). Nenhum alerta emitido.",
                eventId, event.getDangerLevel(), Constants.METEOR_DANGER_THRESHOLD);
            return;
        }

        AlertType  alertType  = mapEventTypeToAlertType(event.getType());
        AlertLevel alertLevel = event.getDangerLevel() >= 9.0 ? AlertLevel.CRITICAL : AlertLevel.HIGH;

        AlertRequest alertRequest = new AlertRequest(
            alertType, alertLevel,
            "Evento espacial detectado: [" + event.getType() + "] " + event.getDescription(),
            habitatId
        );

        // RN10 – alertas críticos possuem prioridade máxima;
        // AlertService fará o triggerAutomaticResponse via Strategy Pattern
        alertService.create(alertRequest);
        log.warn("Alerta {} {} emitido para habitatId={} – evento espacial id={}", alertLevel, alertType, habitatId, eventId);
    }

    private AlertType mapEventTypeToAlertType(SpaceEventType type) {
        return switch (type) {
            case METEOR -> AlertType.METEOR;
            case SOLAR_STORM, SOLAR_FLARE, RADIATION_WAVE -> AlertType.RADIATION;
        };
    }

    private SpaceEvent getOrThrow(Long id) {
        return spaceEventRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("SpaceEvent", id));
    }
}
