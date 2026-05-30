package com.gs.ngn.dto.request;

import com.gs.ngn.domainmodel.enums.SensorType;
import jakarta.validation.constraints.*;

public record SensorRequest(
    @NotBlank String name,
    @NotNull SensorType type,
    @NotNull Double currentValue,
    @NotNull Boolean active,
    @NotNull Double latitude,
    @NotNull Double longitude,
    @NotNull Long moduleId
) {}
