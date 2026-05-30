package com.gs.ngn.validator;

import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.util.Constants;
import org.springframework.stereotype.Component;

@Component
public class EnergyValidator {

    public void validateConsumption(Double consumption) {
        if (consumption != null && consumption < 0) {
            throw new BusinessException("Consumo energético não pode ser negativo.");
        }
    }

    public boolean isEnergyCritical(Double available, Double consumed) {
        if (available == null || consumed == null) return false;
        return consumed > available * Constants.ENERGY_CRITICAL_THRESHOLD;
    }

    public void validateSufficientEnergy(Double availableEnergy, Double additionalConsumption) {
        if (additionalConsumption > availableEnergy) {
            throw new BusinessException(
                "Energia insuficiente para ativar módulo. Disponível: " +
                availableEnergy + " kW, necessário: " + additionalConsumption + " kW."
            );
        }
    }
}
