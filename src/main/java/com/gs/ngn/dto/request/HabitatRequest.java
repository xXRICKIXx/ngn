package com.gs.ngn.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record HabitatRequest(
    @NotBlank(message = "Nome é obrigatório")
    String name,

    @NotNull @Min(value = 1, message = "População deve ser positiva")
    Integer population,

    @NotNull @DecimalMin("0.0") Double availableWater,
    @NotNull @DecimalMin("0.0") Double availableEnergy,

    @Valid @NotNull AtmosphericConditionRequest atmosphericCondition
) {}
