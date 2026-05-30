package com.gs.ngn.integration.nasa;

import com.gs.ngn.domainmodel.enums.SpaceEventType;
import com.gs.ngn.dto.request.SpaceEventRequest;
import com.gs.ngn.service.SpaceEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * RF02 / RF10 – Integração com NASA DONKI para monitorar atividade solar.
 * Busca eventos de Solar Flare e Solar Energetic Particle (tempestades geomagnéticas).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NasaDonkiService {

    private final NasaClient nasaClient;
    private final SpaceEventService spaceEventService;

    @Value("${nasa.donki.base-url}")
    private String donkiBaseUrl;

    /**
     * Busca eventos de Solar Flare das últimas 24h e persiste como SpaceEvent.
     * RN06 – dados atualizados periodicamente.
     */
    public void fetchAndPersistSolarFlares() {
        String url = nasaClient.withKey(donkiBaseUrl + "/FLR?startDate=" + today());
        log.info("[DONKI] Buscando solar flares...");

        try {
            // A NASA DONKI retorna uma lista; usamos o cliente genérico como Object
            Object raw = new org.springframework.web.client.RestTemplate()
                .getForObject(url, Object.class);

            if (raw instanceof List<?> events) {
                events.forEach(e -> persistSolarEvent(e, SpaceEventType.SOLAR_FLARE));
                log.info("[DONKI] {} solar flare(s) processado(s).", events.size());
            }
        } catch (Exception ex) {
            log.warn("[DONKI] Não foi possível buscar solar flares: {}", ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void persistSolarEvent(Object rawEvent, SpaceEventType type) {
        try {
            Map<String, Object> event = (Map<String, Object>) rawEvent;
            String desc = (String) event.getOrDefault("note", "Evento solar detectado pela NASA DONKI");
            String classType = (String) event.getOrDefault("classType", "N/A");

            double danger = estimateDanger(classType);

            spaceEventService.create(new SpaceEventRequest(
                type, "[NASA DONKI] " + classType + " – " + desc,
                danger, LocalDateTime.now(), 0.0, 0.0
            ));
        } catch (Exception ex) {
            log.warn("[DONKI] Falha ao persistir evento solar: {}", ex.getMessage());
        }
    }

    private double estimateDanger(String classType) {
        if (classType == null) return 3.0;
        if (classType.startsWith("X")) return 8.5;
        if (classType.startsWith("M")) return 5.5;
        if (classType.startsWith("C")) return 3.0;
        return 2.0;
    }

    private String today() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
