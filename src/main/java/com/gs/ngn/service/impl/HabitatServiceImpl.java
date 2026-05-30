package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.domainmodel.valueobject.AtmosphericCondition;
import com.gs.ngn.dto.request.AtmosphericConditionRequest;
import com.gs.ngn.dto.request.HabitatRequest;
import com.gs.ngn.dto.response.HabitatResponse;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.HabitatMapper;
import com.gs.ngn.repository.HabitatRepository;
import com.gs.ngn.service.HabitatService;
import com.gs.ngn.validator.AtmosphereValidator;
import com.gs.ngn.validator.HabitatValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HabitatServiceImpl implements HabitatService {

    private final HabitatRepository habitatRepository;
    private final HabitatMapper habitatMapper;
    private final HabitatValidator habitatValidator;
    private final AtmosphereValidator atmosphereValidator;

    @Override
    @Transactional
    public HabitatResponse create(HabitatRequest request) {
        log.info("Criando habitat: {}", request.name());
        habitatValidator.validateCreation(request);

        if (habitatRepository.existsByName(request.name())) {
            throw new BusinessException("Já existe um habitat com o nome: " + request.name());
        }

        Habitat habitat = habitatMapper.toEntity(request);
        atmosphereValidator.validate(habitat.getAtmosphericCondition());

        Habitat saved = habitatRepository.save(habitat);
        log.info("Habitat criado com id={}", saved.getId());
        return habitatMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public HabitatResponse findById(Long id) {
        return habitatMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HabitatResponse> findAll(Pageable pageable) {
        return habitatRepository.findAll(pageable).map(habitatMapper::toResponse);
    }

    @Override
    @Transactional
    public HabitatResponse update(Long id, HabitatRequest request) {
        log.info("Atualizando habitat id={}", id);
        Habitat habitat = getOrThrow(id);
        habitatValidator.validateCreation(request);

        if (!habitat.getName().equals(request.name()) && habitatRepository.existsByName(request.name())) {
            throw new BusinessException("Já existe outro habitat com o nome: " + request.name());
        }

        var ac = request.atmosphericCondition();
        habitat.setName(request.name());
        habitat.setPopulation(request.population());
        habitat.setAvailableWater(request.availableWater());
        habitat.setAvailableEnergy(request.availableEnergy());
        habitat.setAtmosphericCondition(AtmosphericCondition.builder()
            .oxygenLevel(ac.oxygenLevel()).temperature(ac.temperature())
            .humidity(ac.humidity()).pressure(ac.pressure()).radiationLevel(ac.radiationLevel())
            .build());

        atmosphereValidator.validate(habitat.getAtmosphericCondition());
        return habitatMapper.toResponse(habitatRepository.save(habitat));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        log.info("Removendo habitat id={}", id);
        Habitat habitat = getOrThrow(id);
        if (!habitat.getModules().isEmpty()) {
            throw new BusinessException("Não é possível excluir um habitat com módulos ativos. Remova os módulos primeiro.");
        }
        habitatRepository.delete(habitat);
    }

    @Override
    @Transactional
    public HabitatResponse updateAtmosphericCondition(Long id, AtmosphericConditionRequest request) {
        log.info("Atualizando condições atmosféricas do habitat id={}", id);
        Habitat habitat = getOrThrow(id);
        AtmosphericCondition ac = AtmosphericCondition.builder()
            .oxygenLevel(request.oxygenLevel()).temperature(request.temperature())
            .humidity(request.humidity()).pressure(request.pressure()).radiationLevel(request.radiationLevel())
            .build();
        atmosphereValidator.validate(ac);
        habitat.setAtmosphericCondition(ac);
        return habitatMapper.toResponse(habitatRepository.save(habitat));
    }

    private Habitat getOrThrow(Long id) {
        return habitatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", id));
    }
}
