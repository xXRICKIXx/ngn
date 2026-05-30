package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertMapper {
    public AlertResponse toResponse(Alert a) {
        List<AIActionSummaryResponse> actions = a.getAiActions() == null ? List.of() :
            a.getAiActions().stream().map(ai -> new AIActionSummaryResponse(
                ai.getId(), ai.getType(), ai.getExecuted(), ai.getExecutedAt()
            )).toList();

        return new AlertResponse(
            a.getId(), a.getType(), a.getLevel(), a.getMessage(),
            a.getCreatedAt(), a.getResolved(),
            a.getHabitat() != null ? a.getHabitat().getId() : null,
            actions
        );
    }
}
