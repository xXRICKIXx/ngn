package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.Sensor;
import com.gs.ngn.dto.response.SensorResponse;
import org.springframework.stereotype.Component;

@Component
public class SensorMapper {
    public SensorResponse toResponse(Sensor s) {
        return new SensorResponse(
            s.getId(), s.getName(), s.getType(), s.getCurrentValue(), s.getActive(),
            s.getCoordinates() != null ? s.getCoordinates().getLatitude() : null,
            s.getCoordinates() != null ? s.getCoordinates().getLongitude() : null,
            s.getModule() != null ? s.getModule().getId() : null
        );
    }
}
