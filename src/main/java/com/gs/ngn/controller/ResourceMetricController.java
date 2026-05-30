package com.gs.ngn.controller;

import com.gs.ngn.dto.request.ResourceMetricRequest;
import com.gs.ngn.dto.response.ResourceMetricAverageResponse;
import com.gs.ngn.dto.response.ResourceMetricResponse;
import com.gs.ngn.service.ResourceMetricService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/metrics")
@RequiredArgsConstructor
public class ResourceMetricController {

    private final ResourceMetricService resourceMetricService;

    @PostMapping
    public ResponseEntity<ResourceMetricResponse> register(@Valid @RequestBody ResourceMetricRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resourceMetricService.register(request));
    }

    @GetMapping("/habitat/{habitatId}")
    public ResponseEntity<Page<ResourceMetricResponse>> findByHabitat(
            @PathVariable Long habitatId,
            @PageableDefault(size = 10, sort = "collectedAt") Pageable pageable) {
        return ResponseEntity.ok(resourceMetricService.findByHabitat(habitatId, pageable));
    }

    @GetMapping("/habitat/{habitatId}/average")
    public ResponseEntity<ResourceMetricAverageResponse> getAverage(@PathVariable Long habitatId) {
        return ResponseEntity.ok(resourceMetricService.getAverageByHabitat(habitatId));
    }

    @PostMapping("/habitat/{habitatId}/analyze")
    public ResponseEntity<Void> analyze(@PathVariable Long habitatId) {
        resourceMetricService.analyzeAndAlert(habitatId);
        return ResponseEntity.ok().build();
    }
}
