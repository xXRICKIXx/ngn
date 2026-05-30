package com.gs.ngn.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record DashboardResponse(
    LocalDateTime generatedAt,
    Long habitatId,
    String habitatName,
    Integer population,
    AtmosphericConditionResponse atmosphericCondition,
    Double availableEnergy,
    Double activeEnergyConsumption,
    Double availableWater,
    Long openAlerts,
    Long criticalAlerts,
    List<AlertResponse> recentAlerts,
    ResourceMetricAverageResponse resourceAverages,
    List<SpaceEventResponse> highDangerEvents
) {}
