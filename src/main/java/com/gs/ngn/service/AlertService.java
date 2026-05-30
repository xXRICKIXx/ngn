package com.gs.ngn.service;

import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.dto.response.AlertResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertService {
    AlertResponse create(AlertRequest request);
    AlertResponse findById(Long id);
    Page<AlertResponse> findAll(Pageable pageable);
    Page<AlertResponse> findByHabitat(Long habitatId, Pageable pageable);
    Page<AlertResponse> findFiltered(Long habitatId, AlertType type, AlertLevel level, Boolean resolved, Pageable pageable);
    AlertResponse resolve(Long id);
    void triggerAutomaticResponse(Long alertId);
}
