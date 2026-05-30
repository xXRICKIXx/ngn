package com.gs.ngn.service.impl;

import com.gs.ngn.domainmodel.entity.CrewMember;
import com.gs.ngn.dto.request.CrewMemberRequest;
import com.gs.ngn.dto.response.CrewMemberResponse;
import com.gs.ngn.exception.BusinessException;
import com.gs.ngn.exception.ResourceNotFoundException;
import com.gs.ngn.mapper.CrewMemberMapper;
import com.gs.ngn.repository.CrewMemberRepository;
import com.gs.ngn.service.CrewMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewMemberServiceImpl implements CrewMemberService {

    private final CrewMemberRepository crewMemberRepository;
    private final CrewMemberMapper crewMemberMapper;

    @Override
    @Transactional
    public CrewMemberResponse create(CrewMemberRequest request) {
        CrewMember member = CrewMember.builder()
            .name(request.name()).role(request.role())
            .experienceLevel(request.experienceLevel()).build();
        return crewMemberMapper.toResponse(crewMemberRepository.save(member));
    }

    @Override
    @Transactional(readOnly = true)
    public CrewMemberResponse findById(Long id) {
        return crewMemberMapper.toResponse(getOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CrewMemberResponse> findAll(Pageable pageable) {
        return crewMemberRepository.findAll(pageable).map(crewMemberMapper::toResponse);
    }

    @Override
    @Transactional
    public CrewMemberResponse update(Long id, CrewMemberRequest request) {
        CrewMember member = getOrThrow(id);
        member.setName(request.name());
        member.setRole(request.role());
        member.setExperienceLevel(request.experienceLevel());
        return crewMemberMapper.toResponse(crewMemberRepository.save(member));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        CrewMember member = getOrThrow(id);
        if (!member.getModules().isEmpty()) {
            throw new BusinessException("Membro de tripulação está alocado em módulos. Remova-o antes de excluir.");
        }
        crewMemberRepository.delete(member);
    }

    private CrewMember getOrThrow(Long id) {
        return crewMemberRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("CrewMember", id));
    }
}
