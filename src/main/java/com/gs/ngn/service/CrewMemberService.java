package com.gs.ngn.service;

import com.gs.ngn.dto.request.CrewMemberRequest;
import com.gs.ngn.dto.response.CrewMemberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrewMemberService {
    CrewMemberResponse create(CrewMemberRequest request);
    CrewMemberResponse findById(Long id);
    Page<CrewMemberResponse> findAll(Pageable pageable);
    CrewMemberResponse update(Long id, CrewMemberRequest request);
    void delete(Long id);
}
