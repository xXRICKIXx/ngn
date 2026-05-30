package com.gs.ngn.dto.request;

import com.gs.ngn.domainmodel.enums.AIActionType;
import jakarta.validation.constraints.*;

public record AIActionRequest(
    @NotNull AIActionType type,
    @NotBlank @Size(max = 500) String description,
    @NotNull Long alertId
) {}
