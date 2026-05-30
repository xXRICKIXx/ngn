package com.gs.ngn.service;

import com.gs.ngn.dto.request.AIActionRequest;
import com.gs.ngn.dto.response.AIActionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AIActionService {
    AIActionResponse create(AIActionRequest request);
    AIActionResponse findById(Long id);
    Page<AIActionResponse> findAll(Pageable pageable);
    Page<AIActionResponse> findByExecuted(Boolean executed, Pageable pageable);
    AIActionResponse execute(Long id);
}
