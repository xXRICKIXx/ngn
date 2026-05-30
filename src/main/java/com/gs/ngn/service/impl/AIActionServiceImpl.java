package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.dto.request.AIActionRequest;
import com.gs.ngn.dto.response.AIActionResponse;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.AIActionMapper;
import com.gs.ngn.repository.AIActionRepository;
import com.gs.ngn.repository.AlertRepository;
import com.gs.ngn.service.AIActionService;
import com.gs.ngn.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AIActionServiceImpl implements AIActionService {

    private final AIActionRepository aiActionRepository;
    private final AlertRepository alertRepository;
    private final AIActionMapper aiActionMapper;

    @Override
    @Transactional
    public AIActionResponse create(AIActionRequest request) {
        Alert alert = alertRepository.findById(request.alertId())
            .orElseThrow(() -> new ResourceNotFoundException("Alerta", request.alertId()));

        AIAction action = AIAction.builder()
            .type(request.type()).description(request.description())
            .executed(false).alert(alert)
            .build();

        return aiActionMapper.toResponse(aiActionRepository.save(action));
    }

    @Override
    @Transactional(readOnly = true)
    public AIActionResponse findById(Long id) {
        return aiActionMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AIActionResponse> findAll(Pageable pageable) {
        return aiActionRepository.findAll(pageable).map(aiActionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AIActionResponse> findByExecuted(Boolean executed, Pageable pageable) {
        return aiActionRepository.findByExecuted(executed, pageable).map(aiActionMapper::toResponse);
    }

    @Override
    @Transactional
    public AIActionResponse execute(Long id) {
        AIAction action = getOrThrow(id);
        if (Boolean.TRUE.equals(action.getExecuted())) {
            throw new BusinessException("Ação id=" + id + " já foi executada em " + action.getExecutedAt());
        }
        action.setExecuted(true);
        action.setExecutedAt(DateUtils.now());
        // RN08: toda ação automática deve ser registrada
        log.info("[AUDIT] AIAction id={} type={} executada em {}", id, action.getType(), action.getExecutedAt());
        return aiActionMapper.toResponse(aiActionRepository.save(action));
    }

    private AIAction getOrThrow(Long id) {
        return aiActionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AIAction", id));
    }
}
