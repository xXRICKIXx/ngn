package com.gs.ngn.integration.nasa;

import com.gs.ngn.exception.ExternalIntegrationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * RF10 – Utiliza APIs espaciais externas (NASA DONKI e NeoWs).
 */
@Slf4j
@Component
public class NasaClient {

    private final RestTemplate restTemplate;
    private final String apiKey;

    //aqui vai a sua api, voce pode usar a demo ja salva no codigo, quando a demo estoura o limite o spring alerta uma menssagem e não roda o codigo
    public NasaClient(RestTemplate restTemplate,
                      @Value("${nasa.api.key:DEMO_KEY}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }

    /**
     * Executa um GET para a URL informada e retorna o body como Map.
     * Lança ExternalIntegrationException em caso de falha.
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String url) {
        log.debug("[NASA] GET {}", url);
        try {
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            if (response == null) throw new ExternalIntegrationException("Resposta vazia da NASA API: " + url);
            return response;
        } catch (RestClientException e) {
            log.error("[NASA] Falha ao chamar {}: {}", url, e.getMessage());
            throw new ExternalIntegrationException("Falha na integração com a NASA API: " + e.getMessage(), e);
        }
    }

    public String withKey(String baseUrl) {
        return baseUrl + (baseUrl.contains("?") ? "&" : "?") + "api_key=" + apiKey;
    }
}
