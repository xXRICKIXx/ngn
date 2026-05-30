package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.Module;
import com.gs.ngn.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ModuleMapper {

    public ModuleResponse toResponse(Module m) {
        List<SensorSummaryResponse> sensors = m.getSensors() == null ? List.of() :
            m.getSensors().stream().map(s -> new SensorSummaryResponse(
                s.getId(), s.getName(), s.getType(), s.getCurrentValue(), s.getActive()
            )).toList();

        List<CrewMemberSummaryResponse> crew = m.getCrewMembers() == null ? List.of() :
            m.getCrewMembers().stream().map(c -> new CrewMemberSummaryResponse(
                c.getId(), c.getName(), c.getRole(), c.getExperienceLevel()
            )).toList();

        return new ModuleResponse(
            m.getId(), m.getName(), m.getType(), m.isActive(),
            m.getEnergyConsumption(),
            m.getHabitat() != null ? m.getHabitat().getId() : null,
            sensors, crew
        );
    }
}
