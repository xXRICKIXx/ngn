package com.gs.ngn.dto.request;

import jakarta.validation.constraints.*;

public record CrewMemberRequest(
    @NotBlank String name,
    @NotBlank String role,
    @NotNull @Min(1) @Max(10) Integer experienceLevel
) {}
