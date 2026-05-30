package com.gs.ngn.dto.response;

import java.util.List;

public record CrewMemberResponse(
    Long id,
    String name,
    String role,
    Integer experienceLevel,
    List<Long> moduleIds
) {}
