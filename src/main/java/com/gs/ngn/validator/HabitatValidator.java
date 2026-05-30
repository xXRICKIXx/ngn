package com.gs.ngn.validator;

import com.gs.ngn.dto.request.HabitatRequest;
import com.gs.ngn.exception.BusinessException;
import org.springframework.stereotype.Component;

@Component
public class HabitatValidator {

    public void validateCreation(HabitatRequest request) {
        if (request.availableWater() != null && request.availableWater() < 0) {
            throw new BusinessException("Reserva de água não pode ser negativa.");
        }
        if (request.availableEnergy() != null && request.availableEnergy() < 0) {
            throw new BusinessException("Reserva de energia não pode ser negativa.");
        }
        if (request.population() != null && request.population() <= 0) {
            throw new BusinessException("População deve ser maior que zero.");
        }
    }

    public void validateEnergyCapacity(Double availableEnergy, Double requiredEnergy) {
        if (requiredEnergy > availableEnergy) {
            throw new BusinessException(
                "Energia insuficiente no habitat. Disponível: " + availableEnergy +
                " kW, Necessário: " + requiredEnergy + " kW."
            );
        }
    }
}
