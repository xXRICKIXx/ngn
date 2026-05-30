package com.gs.ngn.dto.request;

import com.gs.ngn.domainmodel.enums.SpaceEventType;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record SpaceEventRequest(
    @NotNull SpaceEventType type,
    @NotBlank @Size(max = 1000) String description,
    @NotNull @DecimalMin("0.0") @DecimalMax("10.0") Double dangerLevel,
    @NotNull LocalDateTime eventDate,
    @NotNull Double latitude,
    @NotNull Double longitude
) {}
