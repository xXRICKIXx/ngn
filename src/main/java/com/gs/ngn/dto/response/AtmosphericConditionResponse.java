package com.gs.ngn.dto.response;

public record AtmosphericConditionResponse(
    Double oxygenLevel,
    Double temperature,
    Double humidity,
    Double pressure,
    Double radiationLevel
) {}
