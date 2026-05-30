package com.gs.ngn.service;

import com.gs.ngn.domainmodel.enums.ModuleType;
import com.gs.ngn.dto.request.ModuleRequest;
import com.gs.ngn.dto.response.ModuleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ModuleService {
    ModuleResponse create(ModuleRequest request);
    ModuleResponse findById(Long id);
    Page<ModuleResponse> findAll(Pageable pageable);
    Page<ModuleResponse> findByHabitat(Long habitatId, Pageable pageable);
    ModuleResponse update(Long id, ModuleRequest request);
    void delete(Long id);
    ModuleResponse activate(Long id);
    ModuleResponse deactivate(Long id);
    ModuleResponse assignCrewMember(Long moduleId, Long crewMemberId);
    ModuleResponse removeCrewMember(Long moduleId, Long crewMemberId);
    Double getTotalActiveConsumption(Long habitatId);
}
