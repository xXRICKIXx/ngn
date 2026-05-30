package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.ResourceMetric;
import com.gs.ngn.dto.response.ResourceMetricResponse;
import org.springframework.stereotype.Component;

@Component
public class ResourceMetricMapper {
    public ResourceMetricResponse toResponse(ResourceMetric rm) {
        return new ResourceMetricResponse(
            rm.getId(), rm.getOxygenLevel(), rm.getWaterLevel(), rm.getPressure(),
            rm.getTemperature(), rm.getHumidity(), rm.getAirQuality(), rm.getFoodProduction(),
            rm.getCollectedAt(), rm.getHabitat() != null ? rm.getHabitat().getId() : null
        );
    }
}
