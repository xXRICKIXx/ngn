package com.gs.ngn.service.strategy;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AIActionType;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RN04 – Em caso de falha energética, ativa backup de energia e reduz
 * consumo, priorizando setores essenciais (LIFE_SUPPORT, ENERGY, DEFENSE).
 */
@Component
public class EnergyFailureStrategy implements AIResponseStrategy {

    @Override
    public AlertType getSupportedAlertType() { return AlertType.ENERGY; }

    @Override
    public List<AIAction> execute(Alert alert) {
        return List.of(
            AIAction.builder()
                .type(AIActionType.ACTIVATE_BACKUP_POWER)
                .description("Sistema de energia de reserva ativado. Habitat: " + alert.getHabitat().getName())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build(),
            AIAction.builder()
                .type(AIActionType.REDUCE_CONSUMPTION)
                .description("Redução de consumo aplicada – módulos não-essenciais desligados. Alerta #" + alert.getId())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build()
        );
    }
}
