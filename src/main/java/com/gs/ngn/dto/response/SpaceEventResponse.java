package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.SpaceEventType;
import java.time.LocalDateTime;

public record SpaceEventResponse(
    Long id,
    SpaceEventType type,
    String description,
    Double dangerLevel,
    LocalDateTime eventDate,
    Double latitude,
    Double longitude
) {}
