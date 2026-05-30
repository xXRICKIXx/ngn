package com.gs.ngn.dto.request;

import com.gs.ngn.domainmodel.enums.ModuleType;
import jakarta.validation.constraints.*;

public record ModuleRequest(
    @NotBlank String name,
    @NotNull ModuleType type,
    boolean active,
    @NotNull @DecimalMin("0.0") Double energyConsumption,
    @NotNull Long habitatId
) {}
