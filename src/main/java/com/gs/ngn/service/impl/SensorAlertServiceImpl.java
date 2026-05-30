package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.Sensor;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.repository.SensorRepository;
import com.gs.ngn.service.AlertService;
import com.gs.ngn.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Analisa leituras de todos os sensores ativos e dispara alertas
 * quando limites das regras de negócio são violados.
 * RF01 (O₂), RF02 (radiação solar), RF04 (energia), RF05 (água).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertServiceImpl {

    private final SensorRepository sensorRepository;
    private final AlertService alertService;

    // Limites de temperatura (°C)
    private static final double MIN_TEMPERATURE = -20.0;
    private static final double MAX_TEMPERATURE = Constants.MAX_TEMPERATURE_C;

    // Limite de qualidade do ar (AQI)
    private static final double MAX_AIR_QUALITY_AQI = 150.0;

    // Limite mínimo de produção agrícola (kg/dia)
    private static final double MIN_FOOD_PRODUCTION = 10.0;

    @Transactional
    public void analyzeAllActiveSensors() {
        List<Sensor> activeSensors = sensorRepository.findByActiveTrue();
        log.info("[SCHEDULER] Analisando {} sensores ativos.", activeSensors.size());
        for (Sensor sensor : activeSensors) {
            try {
                analyzeSensor(sensor);
            } catch (Exception e) {
                log.error("[SCHEDULER] Erro ao analisar sensor id={}: {}", sensor.getId(), e.getMessage());
            }
        }
    }

    private void analyzeSensor(Sensor sensor) {
        Long habitatId = sensor.getModule().getHabitat().getId();
        Double value   = sensor.getCurrentValue();
        if (value == null) return;

        switch (sensor.getType()) {

            // RF01 / RN02 – Oxigênio crítico
            case OXYGEN -> {
                if (value < Constants.MIN_OXYGEN_LEVEL) {
                    emitAlert(AlertType.OXYGEN, AlertLevel.CRITICAL,
                        String.format("Sensor '%s': O₂=%.2f%% abaixo do mínimo permitido (%.1f%%).",
                            sensor.getName(), value, Constants.MIN_OXYGEN_LEVEL), habitatId);
                }
            }

            // RF02 / RN01 – Radiação solar perigosa
            case RADIATION -> {
                if (value > Constants.MAX_RADIATION_LEVEL) {
                    emitAlert(AlertType.RADIATION, AlertLevel.HIGH,
                        String.format("Sensor '%s': Radiação=%.3f Sv acima do limite (%.1f Sv).",
                            sensor.getName(), value, Constants.MAX_RADIATION_LEVEL), habitatId);
                }
            }

            // RF05 / RN05 – Reservas de água
            case WATER -> {
                if (value < Constants.MIN_WATER_RESERVE_LITERS) {
                    AlertLevel level = value < Constants.MIN_WATER_RESERVE_LITERS * 0.5
                        ? AlertLevel.CRITICAL : AlertLevel.MEDIUM;
                    emitAlert(AlertType.WATER, level,
                        String.format("Sensor '%s': Reserva hídrica=%.1f L abaixo do mínimo (%.0f L).",
                            sensor.getName(), value, Constants.MIN_WATER_RESERVE_LITERS), habitatId);
                }
            }

            // RF04 – Temperatura fora do intervalo seguro
            case TEMPERATURE -> {
                if (value > MAX_TEMPERATURE) {
                    emitAlert(AlertType.RADIATION, AlertLevel.MEDIUM,
                        String.format("Sensor '%s': Temperatura=%.1f°C acima do máximo (%.1f°C).",
                            sensor.getName(), value, MAX_TEMPERATURE), habitatId);
                } else if (value < MIN_TEMPERATURE) {
                    emitAlert(AlertType.ENERGY, AlertLevel.MEDIUM,
                        String.format("Sensor '%s': Temperatura=%.1f°C abaixo do mínimo (%.1f°C).",
                            sensor.getName(), value, MIN_TEMPERATURE), habitatId);
                }
            }

            // Pressão atmosférica crítica
            case PRESSURE -> {
                if (value < Constants.MIN_PRESSURE_KPA) {
                    emitAlert(AlertType.OXYGEN, AlertLevel.HIGH,
                        String.format("Sensor '%s': Pressão=%.1f kPa abaixo do mínimo (%.1f kPa).",
                            sensor.getName(), value, Constants.MIN_PRESSURE_KPA), habitatId);
                }
            }

            // RF06 / RN05 – Produção agrícola insuficiente
            case FOOD_PRODUCTION -> {
                if (value < MIN_FOOD_PRODUCTION) {
                    emitAlert(AlertType.WATER, AlertLevel.MEDIUM,
                        String.format("Sensor '%s': Produção agrícola=%.1f kg/dia abaixo do mínimo (%.0f kg/dia). " +
                                      "Verifique disponibilidade hídrica (RN05).",
                            sensor.getName(), value, MIN_FOOD_PRODUCTION), habitatId);
                }
            }

            // Qualidade do ar degradada
            case AIR_QUALITY -> {
                if (value > MAX_AIR_QUALITY_AQI) {
                    emitAlert(AlertType.OXYGEN, AlertLevel.MEDIUM,
                        String.format("Sensor '%s': Qualidade do ar AQI=%.0f acima do limite (%.0f).",
                            sensor.getName(), value, MAX_AIR_QUALITY_AQI), habitatId);
                }
            }

            // HUMIDITY: informativo – sem limiar de alerta obrigatório
            case HUMIDITY -> log.trace("Sensor HUMIDITY id={} value={} – sem limiar de alerta.", sensor.getId(), value);

            default -> log.trace("Sensor tipo={} id={} – sem análise configurada.", sensor.getType(), sensor.getId());
        }
    }

    private void emitAlert(AlertType type, AlertLevel level, String message, Long habitatId) {
        log.warn("[SENSOR ALERT] {} {} – {}", level, type, message);
        alertService.create(new AlertRequest(type, level, message, habitatId));
    }
}
