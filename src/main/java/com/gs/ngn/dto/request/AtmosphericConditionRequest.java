package com.gs.ngn.dto.request;

import jakarta.validation.constraints.*;

public record AtmosphericConditionRequest(
    @NotNull @DecimalMin("0.0") @DecimalMax("100.0") Double oxygenLevel,
    @NotNull Double temperature,
    @NotNull @DecimalMin("0.0") @DecimalMax("100.0") Double humidity,
    @NotNull @DecimalMin("0.0") Double pressure,
    @NotNull @DecimalMin("0.0") Double radiationLevel
) {}
