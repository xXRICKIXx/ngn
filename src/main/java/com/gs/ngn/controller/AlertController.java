package com.gs.ngn.controller;

import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import com.gs.ngn.dto.request.AlertRequest;
import com.gs.ngn.dto.response.AlertResponse;
import com.gs.ngn.service.AlertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @PostMapping
    public ResponseEntity<AlertResponse> create(@Valid @RequestBody AlertRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AlertResponse>> findAll(@PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(alertService.findAll(pageable));
    }

    @GetMapping("/habitat/{habitatId}")
    public ResponseEntity<Page<AlertResponse>> findByHabitat(
            @PathVariable Long habitatId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(alertService.findByHabitat(habitatId, pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<AlertResponse>> findFiltered(
            @RequestParam(required = false) Long habitatId,
            @RequestParam(required = false) AlertType type,
            @RequestParam(required = false) AlertLevel level,
            @RequestParam(required = false) Boolean resolved,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(alertService.findFiltered(habitatId, type, level, resolved, pageable));
    }

    @PatchMapping("/{id}/resolve")
    public ResponseEntity<AlertResponse> resolve(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.resolve(id));
    }

    @PostMapping("/{id}/trigger-response")
    public ResponseEntity<Void> triggerResponse(@PathVariable Long id) {
        alertService.triggerAutomaticResponse(id);
        return ResponseEntity.ok().build();
    }
}
