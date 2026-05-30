package com.gs.ngn.scheduler;

import com.gs.ngn.integration.nasa.NasaDonkiService;
import com.gs.ngn.integration.nasa.NasaNeoWsService;
import com.gs.ngn.service.impl.SensorAlertServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * RN06 – Dados das APIs deverão ser atualizados periodicamente.
 * RF06/RF08 – Monitoramento contínuo e em tempo real.
 * RF10 – Consome APIs espaciais externas (NASA DONKI e NeoWs).
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpaceMonitoringScheduler {

    private final SensorAlertServiceImpl sensorAlertService;
    private final NasaDonkiService       nasaDonkiService;
    private final NasaNeoWsService       nasaNeoWsService;

    /**
     * A cada 5 minutos: analisa todos os sensores ativos e dispara alertas.
     * RF01-RF05 / RN01-RN05.
     */
    @Scheduled(fixedRateString = "${scheduling.space-monitor.fixed-rate-ms:300000}")
    public void monitorSensors() {
        log.info("[SCHEDULER] Ciclo de monitoramento de sensores iniciado.");
        try {
            sensorAlertService.analyzeAllActiveSensors();
        } catch (Exception e) {
            log.error("[SCHEDULER] Erro no monitoramento de sensores: {}", e.getMessage(), e);
        }
        log.info("[SCHEDULER] Ciclo de monitoramento de sensores concluído.");
    }

    /**
     * A cada 1 hora: sincroniza eventos solares da NASA DONKI.
     * RF02 / RF10 / RN06.
     */
    @Scheduled(fixedRateString = "3600000")
    public void syncSolarEvents() {
        log.info("[SCHEDULER] Sincronizando eventos solares com NASA DONKI...");
        try {
            nasaDonkiService.fetchAndPersistSolarFlares();
        } catch (Exception e) {
            log.error("[SCHEDULER] Falha ao sincronizar NASA DONKI: {}", e.getMessage(), e);
        }
    }

    /**
     * A cada 2 horas: verifica asteroides próximos (NEO) via NASA NeoWs.
     * RF03 / RF10 / RN03 / RN06.
     */
    @Scheduled(fixedRateString = "7200000")
    public void syncNearEarthObjects() {
        log.info("[SCHEDULER] Verificando Near-Earth Objects com NASA NeoWs...");
        try {
            nasaNeoWsService.fetchAndPersistNearEarthObjects();
        } catch (Exception e) {
            log.error("[SCHEDULER] Falha ao sincronizar NASA NeoWs: {}", e.getMessage(), e);
        }
    }
}
