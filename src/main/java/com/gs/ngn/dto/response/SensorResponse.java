package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.SensorType;

public record SensorResponse(
    Long id,
    String name,
    SensorType type,
    Double currentValue,
    Boolean active,
    Double latitude,
    Double longitude,
    Long moduleId
) {}
