package com.gs.ngn.controller;

import com.gs.ngn.dto.request.SensorRequest;
import com.gs.ngn.dto.response.SensorResponse;
import com.gs.ngn.service.SensorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    public ResponseEntity<SensorResponse> create(@Valid @RequestBody SensorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sensorService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SensorResponse>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(sensorService.findAll(pageable));
    }

    @GetMapping("/module/{moduleId}")
    public ResponseEntity<Page<SensorResponse>> findByModule(
            @PathVariable Long moduleId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(sensorService.findByModule(moduleId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SensorResponse> update(@PathVariable Long id, @Valid @RequestBody SensorRequest request) {
        return ResponseEntity.ok(sensorService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sensorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/value")
    public ResponseEntity<SensorResponse> updateValue(@PathVariable Long id, @RequestParam Double value) {
        return ResponseEntity.ok(sensorService.updateValue(id, value));
    }
}
