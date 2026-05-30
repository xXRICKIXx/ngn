package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.dto.response.AlertResponse;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.AlertMapper;
import com.gs.ngn.repository.AIActionRepository;
import com.gs.ngn.repository.AlertRepository;
import com.gs.ngn.repository.HabitatRepository;
import com.gs.ngn.service.AlertService;
import com.gs.ngn.service.strategy.AIResponseStrategy;
import com.gs.ngn.specification.AlertSpecification;
import com.gs.ngn.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;
    private final HabitatRepository habitatRepository;
    private final AIActionRepository aiActionRepository;
    private final AlertMapper alertMapper;

    // Injeta todas as estratégias via List – Spring resolve automaticamente
    private final List<AIResponseStrategy> strategies;

    // Cache de strategies por tipo de alerta (carregado na injeção)
    private Map<AlertType, AIResponseStrategy> strategyMap;

    private Map<AlertType, AIResponseStrategy> getStrategyMap() {
        if (strategyMap == null) {
            strategyMap = strategies.stream()
                .collect(Collectors.toMap(AIResponseStrategy::getSupportedAlertType, Function.identity()));
        }
        return strategyMap;
    }

    @Override
    @Transactional
    public AlertResponse create(AlertRequest request) {
        log.info("Criando alerta tipo={} nível={} habitatId={}", request.type(), request.level(), request.habitatId());
        Habitat habitat = habitatRepository.findById(request.habitatId())
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", request.habitatId()));

        Alert alert = Alert.builder()
            .type(request.type())
            .level(request.level())
            .message(request.message())
            .createdAt(DateUtils.now())
            .resolved(false)
            .habitat(habitat)
            .build();

        Alert saved = alertRepository.save(alert);
        log.info("Alerta criado id={}. Disparando resposta automática...", saved.getId());

        // RN08 + RN10: resposta automática para alertas críticos
        if (saved.getLevel() == AlertLevel.CRITICAL || saved.getLevel() == AlertLevel.HIGH) {
            triggerAutomaticResponse(saved.getId());
        }

        return alertMapper.toResponse(alertRepository.findById(saved.getId()).orElse(saved));
    }

    @Override
    @Transactional(readOnly = true)
    public AlertResponse findById(Long id) {
        return alertMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> findAll(Pageable pageable) {
        return alertRepository.findAll(pageable).map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> findByHabitat(Long habitatId, Pageable pageable) {
        return alertRepository.findByHabitatId(habitatId, pageable).map(alertMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AlertResponse> findFiltered(Long habitatId, AlertType type, AlertLevel level, Boolean resolved, Pageable pageable) {
        Specification<Alert> spec = AlertSpecification.fromHabitat(habitatId)
            .and(AlertSpecification.hasType(type))
            .and(AlertSpecification.hasLevel(level))
            .and(AlertSpecification.isResolved(resolved));
        return alertRepository.findAll(spec, pageable).map(alertMapper::toResponse);
    }

    @Override
    @Transactional
    public AlertResponse resolve(Long id) {
        Alert alert = getOrThrow(id);
        if (Boolean.TRUE.equals(alert.getResolved())) {
            throw new BusinessException("Alerta id=" + id + " já está resolvido.");
        }
        alert.setResolved(true);
        log.info("Alerta id={} resolvido.", id);
        return alertMapper.toResponse(alertRepository.save(alert));
    }

    @Override
    @Transactional
    public void triggerAutomaticResponse(Long alertId) {
        Alert alert = getOrThrow(alertId);
        AIResponseStrategy strategy = getStrategyMap().get(alert.getType());

        if (strategy == null) {
            log.warn("Nenhuma estratégia registrada para o tipo de alerta: {}", alert.getType());
            return;
        }

        // RN08: toda ação automática deve ser registrada em log
        List<AIAction> actions = strategy.execute(alert);
        aiActionRepository.saveAll(actions);
        log.info("Estratégia {} executou {} ações para o alerta id={}",
            strategy.getClass().getSimpleName(), actions.size(), alertId);
    }

    private Alert getOrThrow(Long id) {
        return alertRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alerta", id));
    }
}
