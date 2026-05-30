package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.SpaceEvent;
import com.gs.ngn.dto.response.SpaceEventResponse;
import org.springframework.stereotype.Component;

@Component
public class SpaceEventMapper {
    public SpaceEventResponse toResponse(SpaceEvent e) {
        return new SpaceEventResponse(
            e.getId(), e.getType(), e.getDescription(), e.getDangerLevel(), e.getEventDate(),
            e.getCoordinates() != null ? e.getCoordinates().getLatitude() : null,
            e.getCoordinates() != null ? e.getCoordinates().getLongitude() : null
        );
    }
}
