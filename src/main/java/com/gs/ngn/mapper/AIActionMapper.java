package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.dto.response.AIActionResponse;
import org.springframework.stereotype.Component;

@Component
public class AIActionMapper {
    public AIActionResponse toResponse(AIAction ai) {
        return new AIActionResponse(
            ai.getId(), ai.getType(), ai.getDescription(),
            ai.getExecuted(), ai.getExecutedAt(),
            ai.getAlert() != null ? ai.getAlert().getId() : null
        );
    }
}
