package com.gs.ngn.service.strategy;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AIActionType;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RN02 – Quando o oxigênio cair abaixo do limite, aciona protocolos críticos:
 * fecha shields de ventilação externa e emite protocolo de emergência.
 */
@Component
public class LowOxygenStrategy implements AIResponseStrategy {

    @Override
    public AlertType getSupportedAlertType() { return AlertType.OXYGEN; }

    @Override
    public List<AIAction> execute(Alert alert) {
        return List.of(
            AIAction.builder()
                .type(AIActionType.CLOSE_SHIELDS)
                .description("Shields de ventilação fechados para conservar O₂ interno. Alerta #" + alert.getId())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build(),
            AIAction.builder()
                .type(AIActionType.EMERGENCY_PROTOCOL)
                .description("Protocolo de emergência de oxigênio ativado no habitat: " + alert.getHabitat().getName())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build()
        );
    }
}
