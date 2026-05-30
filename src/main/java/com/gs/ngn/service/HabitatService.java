package com.gs.ngn.service;

import com.gs.ngn.dto.request.HabitatRequest;
import com.gs.ngn.dto.response.HabitatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HabitatService {
    HabitatResponse create(HabitatRequest request);
    HabitatResponse findById(Long id);
    Page<HabitatResponse> findAll(Pageable pageable);
    HabitatResponse update(Long id, HabitatRequest request);
    void delete(Long id);
    HabitatResponse updateAtmosphericCondition(Long id, com.gs.ngn.dto.request.AtmosphericConditionRequest request);
}
