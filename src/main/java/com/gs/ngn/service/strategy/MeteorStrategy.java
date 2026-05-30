package com.gs.ngn.service.strategy;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AIActionType;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RN03 – Quando há risco de meteoro, ativa o modo de defesa, fecha shields
 * e emite protocolo de emergência total.
 */
@Component
public class MeteorStrategy implements AIResponseStrategy {

    @Override
    public AlertType getSupportedAlertType() { return AlertType.METEOR; }

    @Override
    public List<AIAction> execute(Alert alert) {
        return List.of(
            AIAction.builder()
                .type(AIActionType.CLOSE_SHIELDS)
                .description("Shields externos fechados em resposta a risco de meteoro. Alerta #" + alert.getId())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build(),
            AIAction.builder()
                .type(AIActionType.LOCK_MODULE)
                .description("Todos os módulos bloqueados – modo de defesa ativo. Habitat: " + alert.getHabitat().getName())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build(),
            AIAction.builder()
                .type(AIActionType.EMERGENCY_PROTOCOL)
                .description("Protocolo de emergência METEOR ativado. Aguardar sinal de liberação.")
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build()
        );
    }
}
