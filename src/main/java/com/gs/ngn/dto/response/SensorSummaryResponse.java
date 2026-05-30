package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.SensorType;

public record SensorSummaryResponse(Long id, String name, SensorType type, Double currentValue, Boolean active) {}
