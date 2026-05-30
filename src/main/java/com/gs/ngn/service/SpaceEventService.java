package com.gs.ngn.service;

import com.gs.ngn.domainmodel.enums.SpaceEventType;
import com.gs.ngn.dto.request.SpaceEventRequest;
import com.gs.ngn.dto.response.SpaceEventResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface SpaceEventService {
    SpaceEventResponse create(SpaceEventRequest request);
    SpaceEventResponse findById(Long id);
    Page<SpaceEventResponse> findAll(Pageable pageable);
    Page<SpaceEventResponse> findFiltered(SpaceEventType type, Double minDanger, LocalDateTime from, LocalDateTime to, Pageable pageable);
    List<SpaceEventResponse> findHighDangerEvents();
    void processDangerousEvent(Long eventId, Long habitatId);
}
