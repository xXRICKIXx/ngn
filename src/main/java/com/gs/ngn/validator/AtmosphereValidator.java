package com.gs.ngn.validator;

import com.gs.ngn.domainmodel.valueobject.AtmosphericCondition;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.util.Constants;
import org.springframework.stereotype.Component;

@Component
public class AtmosphereValidator {

    public void validate(AtmosphericCondition ac) {
        if (ac == null) return;

        if (ac.getOxygenLevel() < Constants.MIN_OXYGEN_LEVEL) {
            throw new BusinessException(
                "Nível de oxigênio crítico: " + ac.getOxygenLevel() +
                "%. Mínimo permitido: " + Constants.MIN_OXYGEN_LEVEL + "%."
            );
        }
        if (ac.getRadiationLevel() > Constants.MAX_RADIATION_LEVEL) {
            throw new BusinessException(
                "Nível de radiação perigoso: " + ac.getRadiationLevel() +
                " Sv. Máximo permitido: " + Constants.MAX_RADIATION_LEVEL + " Sv."
            );
        }
        if (ac.getPressure() < Constants.MIN_PRESSURE_KPA) {
            throw new BusinessException(
                "Pressão atmosférica crítica: " + ac.getPressure() + " kPa."
            );
        }
    }

    public boolean isOxygenCritical(Double oxygenLevel) {
        return oxygenLevel != null && oxygenLevel < Constants.MIN_OXYGEN_LEVEL;
    }

    public boolean isRadiationDangerous(Double radiationLevel) {
        return radiationLevel != null && radiationLevel > Constants.MAX_RADIATION_LEVEL;
    }
}
