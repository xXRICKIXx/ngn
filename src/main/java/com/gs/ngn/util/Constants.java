package com.gs.ngn.util;

public class Constants {
    private Constants() {}

    // Atmospheric thresholds
    public static final double MIN_OXYGEN_LEVEL      = 19.5;   // %
    public static final double MAX_RADIATION_LEVEL   = 1.0;    // Sv/h
    public static final double MIN_PRESSURE_KPA      = 70.0;   // kPa
    public static final double MAX_TEMPERATURE_C     = 45.0;   // °C
    public static final double METEOR_DANGER_THRESHOLD = 7.0;  // 0–10 scale

    // Energy
    public static final double ENERGY_CRITICAL_THRESHOLD = 0.90; // 90% consumed
    public static final double MIN_WATER_RESERVE_LITERS  = 500.0;

    // Agriculture (RN05)
    public static final double MIN_WATER_FOR_AGRICULTURE_LITERS = 200.0;

    // Scheduler
    public static final long MONITORING_FIXED_RATE_MS = 300_000L;
}
