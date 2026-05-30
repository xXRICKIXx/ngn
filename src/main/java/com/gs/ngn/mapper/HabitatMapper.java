package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.domainmodel.valueobject.AtmosphericCondition;
import com.gs.ngn.dto.request.HabitatRequest;
import com.gs.ngn.dto.response.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HabitatMapper {

    public Habitat toEntity(HabitatRequest req) {
        var ac = req.atmosphericCondition();
        return Habitat.builder()
            .name(req.name())
            .population(req.population())
            .availableWater(req.availableWater())
            .availableEnergy(req.availableEnergy())
            .atmosphericCondition(AtmosphericCondition.builder()
                .oxygenLevel(ac.oxygenLevel())
                .temperature(ac.temperature())
                .humidity(ac.humidity())
                .pressure(ac.pressure())
                .radiationLevel(ac.radiationLevel())
                .build())
            .build();
    }

    public HabitatResponse toResponse(Habitat h) {
        var ac = h.getAtmosphericCondition();
        List<ModuleSummaryResponse> modules = h.getModules() == null ? List.of() :
            h.getModules().stream().map(m -> new ModuleSummaryResponse(
                m.getId(), m.getName(), m.getType(), m.isActive(), m.getEnergyConsumption()
            )).toList();

        return new HabitatResponse(
            h.getId(), h.getName(), h.getPopulation(),
            h.getAvailableWater(), h.getAvailableEnergy(),
            ac == null ? null : new AtmosphericConditionResponse(
                ac.getOxygenLevel(), ac.getTemperature(), ac.getHumidity(),
                ac.getPressure(), ac.getRadiationLevel()),
            modules
        );
    }
}
