package com.gs.ngn.service;

import com.gs.ngn.dto.request.SensorRequest;
import com.gs.ngn.dto.response.SensorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SensorService {
    SensorResponse create(SensorRequest request);
    SensorResponse findById(Long id);
    Page<SensorResponse> findAll(Pageable pageable);
    Page<SensorResponse> findByModule(Long moduleId, Pageable pageable);
    SensorResponse update(Long id, SensorRequest request);
    void delete(Long id);
    SensorResponse updateValue(Long id, Double newValue);
}
