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
 * RF03 / RF10 – Integração com NASA NeoWs para monitorar asteroides e meteoros próximos.
 * RN03 – quando há risco de meteoro, aciona modo de defesa via SpaceEventService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NasaNeoWsService {

    private final NasaClient nasaClient;
    private final SpaceEventService spaceEventService;

    @Value("${nasa.neows.base-url}")
    private String neoWsBaseUrl;

    /**
     * Busca Near-Earth Objects potencialmente perigosos para hoje e persiste como SpaceEvent.
     */
    @SuppressWarnings("unchecked")
    public void fetchAndPersistNearEarthObjects() {
        String today = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String url = nasaClient.withKey(neoWsBaseUrl + "/feed?start_date=" + today + "&end_date=" + today);
        log.info("[NEOWs] Buscando Near-Earth Objects...");

        try {
            Map<String, Object> response = nasaClient.get(url);
            Map<String, Object> neosByDate = (Map<String, Object>) response.get("near_earth_objects");

            if (neosByDate == null) return;

            neosByDate.values().forEach(dayList -> {
                if (dayList instanceof List<?> neos) {
                    neos.forEach(neoRaw -> persistNeo((Map<String, Object>) neoRaw));
                }
            });
        } catch (Exception ex) {
            log.warn("[NEOWs] Não foi possível buscar NEOs: {}", ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void persistNeo(Map<String, Object> neo) {
        try {
            boolean hazardous = Boolean.TRUE.equals(neo.get("is_potentially_hazardous_asteroid"));
            if (!hazardous) return;

            String name = (String) neo.getOrDefault("name", "Asteroide desconhecido");
            Map<String, Object> closeApproach = ((List<Map<String, Object>>) neo.get("close_approach_data")).get(0);
            String relVelocity = (String) ((Map<?, ?>) closeApproach.get("relative_velocity")).get("kilometers_per_hour");
            double danger = Double.parseDouble(relVelocity) > 50000 ? 9.0 : 7.0;

            spaceEventService.create(new SpaceEventRequest(
                SpaceEventType.METEOR,
                "[NASA NeoWs] Asteroide potencialmente perigoso: " + name,
                danger, LocalDateTime.now(), 0.0, 0.0
            ));
            log.warn("[NEOWs] Asteroide perigoso detectado: {} dangerLevel={}", name, danger);
        } catch (Exception ex) {
            log.warn("[NEOWs] Erro ao processar NEO: {}", ex.getMessage());
        }
    }
}
