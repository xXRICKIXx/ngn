package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.Module;
import com.gs.ngn.domainmodel.entity.Sensor;
import com.gs.ngn.domainmodel.valueobject.Coordinates;
import com.gs.ngn.dto.request.SensorRequest;
import com.gs.ngn.dto.response.SensorResponse;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.SensorMapper;
import com.gs.ngn.repository.ModuleRepository;
import com.gs.ngn.repository.SensorRepository;
import com.gs.ngn.service.SensorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorServiceImpl implements SensorService {

    private final SensorRepository sensorRepository;
    private final ModuleRepository moduleRepository;
    private final SensorMapper sensorMapper;

    @Override
    @Transactional
    public SensorResponse create(SensorRequest request) {
        Module module = moduleRepository.findById(request.moduleId())
            .orElseThrow(() -> new ResourceNotFoundException("Módulo", request.moduleId()));

        Sensor sensor = Sensor.builder()
            .name(request.name()).type(request.type())
            .currentValue(request.currentValue()).active(request.active())
            .coordinates(new Coordinates(request.latitude(), request.longitude()))
            .module(module)
            .build();

        return sensorMapper.toResponse(sensorRepository.save(sensor));
    }

    @Override
    @Transactional(readOnly = true)
    public SensorResponse findById(Long id) {
        return sensorMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponse> findAll(Pageable pageable) {
        return sensorRepository.findAll(pageable).map(sensorMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SensorResponse> findByModule(Long moduleId, Pageable pageable) {
        return sensorRepository.findAll(
            (root, q, cb) -> cb.equal(root.get("module").get("id"), moduleId), pageable
        ).map(sensorMapper::toResponse);
    }

    @Override
    @Transactional
    public SensorResponse update(Long id, SensorRequest request) {
        Sensor sensor = getOrThrow(id);
        Module module = moduleRepository.findById(request.moduleId())
            .orElseThrow(() -> new ResourceNotFoundException("Módulo", request.moduleId()));
        sensor.setName(request.name()); sensor.setType(request.type());
        sensor.setCurrentValue(request.currentValue()); sensor.setActive(request.active());
        sensor.setCoordinates(new Coordinates(request.latitude(), request.longitude()));
        sensor.setModule(module);
        return sensorMapper.toResponse(sensorRepository.save(sensor));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        sensorRepository.delete(getOrThrow(id));
    }

    @Override
    @Transactional
    public SensorResponse updateValue(Long id, Double newValue) {
        Sensor sensor = getOrThrow(id);
        log.debug("Sensor id={} valor atualizado: {} -> {}", id, sensor.getCurrentValue(), newValue);
        sensor.setCurrentValue(newValue);
        return sensorMapper.toResponse(sensorRepository.save(sensor));
    }

    private Sensor getOrThrow(Long id) {
        return sensorRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sensor", id));
    }
}
