package com.gs.ngn.dto.response;

import com.gs.ngn.domainmodel.enums.ModuleType;

public record ModuleSummaryResponse(Long id, String name, ModuleType type, boolean active, Double energyConsumption) {}
