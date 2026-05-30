package com.gs.ngn.dto.response;

import java.util.List;

public record HabitatResponse(
    Long id,
    String name,
    Integer population,
    Double availableWater,
    Double availableEnergy,
    AtmosphericConditionResponse atmosphericCondition,
    List<ModuleSummaryResponse> modules
) {}
