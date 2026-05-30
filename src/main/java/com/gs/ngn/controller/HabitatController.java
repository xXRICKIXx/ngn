package com.gs.ngn.controller;

import com.gs.ngn.dto.request.AtmosphericConditionRequest;
import com.gs.ngn.dto.request.HabitatRequest;
import com.gs.ngn.dto.response.HabitatResponse;
import com.gs.ngn.service.HabitatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/habitats")
@RequiredArgsConstructor
public class HabitatController {

    private final HabitatService habitatService;

    @PostMapping
    public ResponseEntity<HabitatResponse> create(@Valid @RequestBody HabitatRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(habitatService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HabitatResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(habitatService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<HabitatResponse>> findAll(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        return ResponseEntity.ok(habitatService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HabitatResponse> update(@PathVariable Long id, @Valid @RequestBody HabitatRequest request) {
        return ResponseEntity.ok(habitatService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitatService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/atmosphere")
    public ResponseEntity<HabitatResponse> updateAtmosphere(
            @PathVariable Long id,
            @Valid @RequestBody AtmosphericConditionRequest request) {
        return ResponseEntity.ok(habitatService.updateAtmosphericCondition(id, request));
    }
}
