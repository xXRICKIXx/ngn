package com.gs.ngn.service.strategy;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AIActionType;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.util.DateUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RN01 – Quando o nível de radiação for alto, ativa shields de proteção
 * e bloqueia módulos não-essenciais.
 */
@Component
public class RadiationStrategy implements AIResponseStrategy {

    @Override
    public AlertType getSupportedAlertType() { return AlertType.RADIATION; }

    @Override
    public List<AIAction> execute(Alert alert) {
        return List.of(
            AIAction.builder()
                .type(AIActionType.CLOSE_SHIELDS)
                .description("Shields de radiação ativados automaticamente. Habitat: " + alert.getHabitat().getName())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build(),
            AIAction.builder()
                .type(AIActionType.LOCK_MODULE)
                .description("Módulos não-essenciais bloqueados durante evento de radiação. Alerta #" + alert.getId())
                .executed(true)
                .executedAt(DateUtils.now())
                .alert(alert)
                .build()
        );
    }
}
