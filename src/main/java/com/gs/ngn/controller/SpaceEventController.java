package com.gs.ngn.controller;

import com.gs.ngn.domainmodel.enums.SpaceEventType;
import com.gs.ngn.dto.request.SpaceEventRequest;
import com.gs.ngn.dto.response.SpaceEventResponse;
import com.gs.ngn.service.SpaceEventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/space-events")
@RequiredArgsConstructor
public class SpaceEventController {

    private final SpaceEventService spaceEventService;

    @PostMapping
    public ResponseEntity<SpaceEventResponse> create(@Valid @RequestBody SpaceEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(spaceEventService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpaceEventResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(spaceEventService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<SpaceEventResponse>> findAll(@PageableDefault(size = 10, sort = "eventDate") Pageable pageable) {
        return ResponseEntity.ok(spaceEventService.findAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<SpaceEventResponse>> findFiltered(
            @RequestParam(required = false) SpaceEventType type,
            @RequestParam(required = false) Double minDanger,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(spaceEventService.findFiltered(type, minDanger, from, to, pageable));
    }

    @GetMapping("/high-danger")
    public ResponseEntity<List<SpaceEventResponse>> findHighDanger() {
        return ResponseEntity.ok(spaceEventService.findHighDangerEvents());
    }

    @PostMapping("/{eventId}/process/{habitatId}")
    public ResponseEntity<Void> processForHabitat(@PathVariable Long eventId, @PathVariable Long habitatId) {
        spaceEventService.processDangerousEvent(eventId, habitatId);
        return ResponseEntity.ok().build();
    }
}
