package com.gs.ngn.controller;

import com.gs.ngn.dto.request.AIActionRequest;
import com.gs.ngn.dto.response.AIActionResponse;
import com.gs.ngn.service.AIActionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-actions")
@RequiredArgsConstructor
public class AIController {

    private final AIActionService aiActionService;

    @PostMapping
    public ResponseEntity<AIActionResponse> create(@Valid @RequestBody AIActionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aiActionService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AIActionResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(aiActionService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<AIActionResponse>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(aiActionService.findAll(pageable));
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<AIActionResponse>> findPending(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(aiActionService.findByExecuted(false, pageable));
    }

    @PatchMapping("/{id}/execute")
    public ResponseEntity<AIActionResponse> execute(@PathVariable Long id) {
        return ResponseEntity.ok(aiActionService.execute(id));
    }
}
