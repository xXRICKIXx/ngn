package com.gs.ngn.dto.response;

public record ResourceMetricAverageResponse(
    Long habitatId,
    String habitatName,
    Double avgOxygenLevel,
    Double avgWaterLevel,
    Double avgPressure,
    Double avgTemperature,
    Double avgHumidity,
    Double avgAirQuality,
    Double avgFoodProduction
) {}
