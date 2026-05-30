package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.AIActionType;
import java.time.LocalDateTime;

public record AIActionSummaryResponse(Long id, AIActionType type, Boolean executed, LocalDateTime executedAt) {}
