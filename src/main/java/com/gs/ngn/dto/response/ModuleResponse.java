package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.ModuleType;
import java.util.List;

public record ModuleResponse(
    Long id,
    String name,
    ModuleType type,
    boolean active,
    Double energyConsumption,
    Long habitatId,
    List<SensorSummaryResponse> sensors,
    List<CrewMemberSummaryResponse> crewMembers
) {}
