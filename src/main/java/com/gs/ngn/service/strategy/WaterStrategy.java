package com.gs.ngn.service.strategy;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AIActionType;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RN05 – Produção agrícola deve respeitar disponibilidade hídrica.
 * Quando a reserva de água cair abaixo do limite, o sistema reduz
 * o consumo não-essencial e emite protocolo de racionamento.
 */
@Component
public class WaterStrategy implements AIResponseStrategy {

    @Override
    public AlertType getSupportedAlertType() { return AlertType.WATER; }

    @Override
    public List<AIAction> execute(Alert alert) {
        return List.of(
            AIAction.builder()
                .type(AIActionType.REDUCE_CONSUMPTION)
                .description("Racionamento de água ativado: módulos de agricultura reduzidos. " +
                             "Habitat: " + alert.getHabitat().getName())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build(),
            AIAction.builder()
                .type(AIActionType.EMERGENCY_PROTOCOL)
                .description("Protocolo de escassez hídrica iniciado. " +
                             "Alerta #" + alert.getId() + " – " + alert.getMessage())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build()
        );
    }
}
