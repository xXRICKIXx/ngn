package com.gs.ngn.service;

import com.gs.ngn.dto.request.ResourceMetricRequest;
import com.gs.ngn.dto.response.ResourceMetricAverageResponse;
import com.gs.ngn.dto.response.ResourceMetricResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResourceMetricService {
    ResourceMetricResponse register(ResourceMetricRequest request);
    Page<ResourceMetricResponse> findByHabitat(Long habitatId, Pageable pageable);
    ResourceMetricAverageResponse getAverageByHabitat(Long habitatId);
    void analyzeAndAlert(Long habitatId);
}
