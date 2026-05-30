package com.gs.ngn.service.strategy;

import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.enums.AlertType;

import java.util.List;

/**
 * Strategy Pattern – define o contrato para as estratégias de resposta
 * automática da IA a cada tipo de alerta de emergência.
 */
public interface AIResponseStrategy {

    /** Tipo de alerta que esta estratégia trata. */
    AlertType getSupportedAlertType();

    /**
     * Executa a resposta automática e retorna as ações geradas,
     * ainda não persistidas (responsabilidade do serviço chamador).
     */
    List<AIAction> execute(Alert alert);
}
