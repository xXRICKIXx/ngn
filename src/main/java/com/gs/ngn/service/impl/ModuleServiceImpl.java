package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.CrewMember;
import com.gs.ngn.domainmodel.entity.Habitat;
import com.gs.ngn.domainmodel.entity.Module;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.domainmodel.enums.ModuleType;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.dto.request.ModuleRequest;
import com.gs.ngn.dto.response.ModuleResponse;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.ModuleMapper;
import com.gs.ngn.repository.CrewMemberRepository;
import com.gs.ngn.repository.HabitatRepository;
import com.gs.ngn.repository.ModuleRepository;
import com.gs.ngn.service.AlertService;
import com.gs.ngn.service.ModuleService;
import com.gs.ngn.specification.ModuleSpecification;
import com.gs.ngn.validator.EnergyValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository     moduleRepository;
    private final HabitatRepository    habitatRepository;
    private final CrewMemberRepository crewMemberRepository;
    private final AlertService         alertService;
    private final ModuleMapper         moduleMapper;
    private final EnergyValidator      energyValidator;

    @Override
    @Transactional
    public ModuleResponse create(ModuleRequest request) {
        log.info("Criando módulo '{}' no habitat id={}", request.name(), request.habitatId());
        Habitat habitat = getHabitatOrThrow(request.habitatId());
        energyValidator.validateConsumption(request.energyConsumption());

        if (request.active()) {
            Double current = moduleRepository.sumActiveEnergyConsumptionByHabitatId(habitat.getId());
            double consumed = current == null ? 0.0 : current;
            energyValidator.validateSufficientEnergy(
                habitat.getAvailableEnergy() - consumed, request.energyConsumption()
            );
            checkAndEmitEnergyAlert(habitat, consumed + request.energyConsumption());
        }

        Module module = Module.builder()
            .name(request.name()).type(request.type())
            .active(request.active()).energyConsumption(request.energyConsumption())
            .habitat(habitat)
            .build();

        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleResponse findById(Long id) {
        return moduleMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleResponse> findAll(Pageable pageable) {
        return moduleRepository.findAll(pageable).map(moduleMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModuleResponse> findByHabitat(Long habitatId, Pageable pageable) {
        return moduleRepository.findAll(ModuleSpecification.fromHabitat(habitatId), pageable)
            .map(moduleMapper::toResponse);
    }

    @Override
    @Transactional
    public ModuleResponse update(Long id, ModuleRequest request) {
        Module module = getOrThrow(id);
        Habitat habitat = getHabitatOrThrow(request.habitatId());
        energyValidator.validateConsumption(request.energyConsumption());
        module.setName(request.name());
        module.setType(request.type());
        module.setActive(request.active());
        module.setEnergyConsumption(request.energyConsumption());
        module.setHabitat(habitat);
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Module module = getOrThrow(id);
        if (isEssential(module.getType()) && module.isActive()) {
            throw new BusinessException(
                "Módulos essenciais ativos (" + module.getType() + ") não podem ser excluídos.");
        }
        moduleRepository.delete(module);
    }

    @Override
    @Transactional
    public ModuleResponse activate(Long id) {
        Module module = getOrThrow(id);
        if (module.isActive()) throw new BusinessException("Módulo id=" + id + " já está ativo.");

        Habitat habitat = module.getHabitat();
        Double current  = moduleRepository.sumActiveEnergyConsumptionByHabitatId(habitat.getId());
        double consumed = current == null ? 0.0 : current;

        // RN04 – validar energia disponível
        energyValidator.validateSufficientEnergy(
            habitat.getAvailableEnergy() - consumed, module.getEnergyConsumption()
        );

        // FIX 7 – emitir alerta se após ativação o consumo atingir nível crítico (RN04)
        checkAndEmitEnergyAlert(habitat, consumed + module.getEnergyConsumption());

        module.setActive(true);
        log.info("Módulo id={} ativado.", id);
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public ModuleResponse deactivate(Long id) {
        Module module = getOrThrow(id);
        if (!module.isActive()) throw new BusinessException("Módulo id=" + id + " já está inativo.");
        if (isEssential(module.getType())) {
            throw new BusinessException(
                "Módulo essencial (" + module.getType() + ") não pode ser desativado manualmente. (RN04)");
        }
        module.setActive(false);
        log.info("Módulo id={} desativado.", id);
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public ModuleResponse assignCrewMember(Long moduleId, Long crewMemberId) {
        Module module = getOrThrow(moduleId);
        CrewMember crew = crewMemberRepository.findById(crewMemberId)
            .orElseThrow(() -> new ResourceNotFoundException("CrewMember", crewMemberId));
        if (module.getCrewMembers().contains(crew)) {
            throw new BusinessException("Membro id=" + crewMemberId + " já alocado neste módulo.");
        }
        module.getCrewMembers().add(crew);
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional
    public ModuleResponse removeCrewMember(Long moduleId, Long crewMemberId) {
        Module module = getOrThrow(moduleId);
        CrewMember crew = crewMemberRepository.findById(crewMemberId)
            .orElseThrow(() -> new ResourceNotFoundException("CrewMember", crewMemberId));
        module.getCrewMembers().remove(crew);
        return moduleMapper.toResponse(moduleRepository.save(module));
    }

    @Override
    @Transactional(readOnly = true)
    public Double getTotalActiveConsumption(Long habitatId) {
        Double total = moduleRepository.sumActiveEnergyConsumptionByHabitatId(habitatId);
        return total != null ? total : 0.0;
    }

    // ─── helpers ─────────────────────────────────────────────────────────

    /**
     * RN04 / FIX 7 – Emite alerta de energia se consumo pós-ativação ultrapassar
     * 90% da capacidade disponível (Constants.ENERGY_CRITICAL_THRESHOLD).
     */
    private void checkAndEmitEnergyAlert(Habitat habitat, double newTotalConsumption) {
        if (energyValidator.isEnergyCritical(habitat.getAvailableEnergy(), newTotalConsumption)) {
            log.warn("[ENERGY] Consumo crítico detectado no habitat '{}': {:.2f} / {} kW",
                habitat.getName(), newTotalConsumption, habitat.getAvailableEnergy());
            alertService.create(new AlertRequest(
                AlertType.ENERGY, AlertLevel.HIGH,
                String.format("Consumo energético crítico: %.1f kW de %.1f kW disponíveis (%.0f%%). " +
                              "Setores essenciais priorizados (RN04).",
                    newTotalConsumption, habitat.getAvailableEnergy(),
                    (newTotalConsumption / habitat.getAvailableEnergy()) * 100),
                habitat.getId()
            ));
        }
    }

    private Module getOrThrow(Long id) {
        return moduleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Módulo", id));
    }

    private Habitat getHabitatOrThrow(Long id) {
        return habitatRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Habitat", id));
    }

    private boolean isEssential(ModuleType type) {
        return type == ModuleType.LIFE_SUPPORT
            || type == ModuleType.ENERGY
            || type == ModuleType.DEFENSE;
    }
}
