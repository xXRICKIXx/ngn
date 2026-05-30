package com.gs.ngn.dto.response;

import java.time.LocalDateTime;

public record ResourceMetricResponse(
    Long id,
    Double oxygenLevel,
    Double waterLevel,
    Double pressure,
    Double temperature,
    Double humidity,
    Double airQuality,
    Double foodProduction,
    LocalDateTime collectedAt,
    Long habitatId
) {}
