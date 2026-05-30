package com.gs.ngn.controller;

import com.gs.ngn.dto.response.DashboardResponse;
import com.gs.ngn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * RF08 – Endpoint de dashboard em tempo real.
 * Consolida: condições atmosféricas, energia, água, alertas, métricas e eventos espaciais.
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/habitat/{habitatId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long habitatId) {
        return ResponseEntity.ok(dashboardService.getDashboard(habitatId));
    }
}
