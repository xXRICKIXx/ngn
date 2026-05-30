package com.gs.ngn.dto.request;

import jakarta.validation.constraints.*;

public record ResourceMetricRequest(
    @NotNull @DecimalMin("0.0") @DecimalMax("100.0") Double oxygenLevel,
    @NotNull @DecimalMin("0.0") Double waterLevel,
    @NotNull @DecimalMin("0.0") Double pressure,
    @NotNull Double temperature,
    @NotNull @DecimalMin("0.0") @DecimalMax("100.0") Double humidity,
    @NotNull @DecimalMin("0.0") @DecimalMax("500.0") Double airQuality,
    @NotNull @DecimalMin("0.0") Double foodProduction,
    @NotNull Long habitatId
) {}
