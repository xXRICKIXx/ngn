package com.gs.ngn.controller;

import com.gs.ngn.dto.request.CrewMemberRequest;
import com.gs.ngn.dto.response.CrewMemberResponse;
import com.gs.ngn.service.CrewMemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/crew-members")
@RequiredArgsConstructor
public class CrewMemberController {

    private final CrewMemberService crewMemberService;

    @PostMapping
    public ResponseEntity<CrewMemberResponse> create(@Valid @RequestBody CrewMemberRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(crewMemberService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrewMemberResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(crewMemberService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<CrewMemberResponse>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(crewMemberService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CrewMemberResponse> update(@PathVariable Long id, @Valid @RequestBody CrewMemberRequest request) {
        return ResponseEntity.ok(crewMemberService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        crewMemberService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
