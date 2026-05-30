package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import java.time.LocalDateTime;
import java.util.List;

public record AlertResponse(
    Long id,
    AlertType type,
    AlertLevel level,
    String message,
    LocalDateTime createdAt,
    Boolean resolved,
    Long habitatId,
    List<AIActionSummaryResponse> aiActions
) {}
