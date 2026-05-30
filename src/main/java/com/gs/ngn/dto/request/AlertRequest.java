package com.gs.ngn.dto.request;

import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import jakarta.validation.constraints.*;

public record AlertRequest(
    @NotNull AlertType type,
    @NotNull AlertLevel level,
    @NotBlank @Size(max = 500) String message,
    @NotNull Long habitatId
) {}
