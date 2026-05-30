package com.gs.ngn.service;

import com.gs.ngn.dto.response.DashboardResponse;

public interface DashboardService {
    DashboardResponse getDashboard(Long habitatId);
}
