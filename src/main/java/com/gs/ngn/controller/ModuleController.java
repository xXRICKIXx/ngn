package com.gs.ngn.controller;

import com.gs.ngn.dto.request.ModuleRequest;
import com.gs.ngn.dto.response.ModuleResponse;
import com.gs.ngn.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<ModuleResponse> create(@Valid @RequestBody ModuleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuleResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ModuleResponse>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(moduleService.findAll(pageable));
    }

    @GetMapping("/habitat/{habitatId}")
    public ResponseEntity<Page<ModuleResponse>> findByHabitat(
            @PathVariable Long habitatId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(moduleService.findByHabitat(habitatId, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModuleResponse> update(@PathVariable Long id, @Valid @RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<ModuleResponse> activate(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.activate(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ModuleResponse> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(moduleService.deactivate(id));
    }

    @PostMapping("/{moduleId}/crew/{crewMemberId}")
    public ResponseEntity<ModuleResponse> assignCrew(@PathVariable Long moduleId, @PathVariable Long crewMemberId) {
        return ResponseEntity.ok(moduleService.assignCrewMember(moduleId, crewMemberId));
    }

    @DeleteMapping("/{moduleId}/crew/{crewMemberId}")
    public ResponseEntity<ModuleResponse> removeCrew(@PathVariable Long moduleId, @PathVariable Long crewMemberId) {
        return ResponseEntity.ok(moduleService.removeCrewMember(moduleId, crewMemberId));
    }

    @GetMapping("/habitat/{habitatId}/consumption")
    public ResponseEntity<Double> getTotalConsumption(@PathVariable Long habitatId) {
        return ResponseEntity.ok(moduleService.getTotalActiveConsumption(habitatId));
    }
}
