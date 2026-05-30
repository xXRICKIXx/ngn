package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.AIActionType;
import java.time.LocalDateTime;

public record AIActionResponse(
    Long id,
    AIActionType type,
    String description,
    Boolean executed,
    LocalDateTime executedAt,
    Long alertId
) {}
