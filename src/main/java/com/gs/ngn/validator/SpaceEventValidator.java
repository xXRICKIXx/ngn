package com.gs.ngn.validator;

import com.gs.ngn.dto.request.SpaceEventRequest;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.util.Constants;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SpaceEventValidator {

    public void validate(SpaceEventRequest request) {
        if (request.dangerLevel() < 0.0 || request.dangerLevel() > 10.0) {
            throw new BusinessException(
                "dangerLevel deve estar entre 0.0 e 10.0. Recebido: " + request.dangerLevel());
        }
        if (request.eventDate() == null) {
            throw new BusinessException("A data do evento é obrigatória.");
        }
        if (request.eventDate().isAfter(LocalDateTime.now().plusDays(30))) {
            throw new BusinessException(
                "Data do evento não pode ser superior a 30 dias no futuro.");
        }
    }

    public boolean isHighDanger(Double dangerLevel) {
        return dangerLevel != null && dangerLevel >= Constants.METEOR_DANGER_THRESHOLD;
    }
}
