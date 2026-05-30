package com.gs.ngn.mapper;

import com.gs.ngn.domainmodel.entity.CrewMember;
import com.gs.ngn.dto.response.CrewMemberResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CrewMemberMapper {
    public CrewMemberResponse toResponse(CrewMember c) {
        List<Long> moduleIds = c.getModules() == null ? List.of() :
            c.getModules().stream().map(m -> m.getId()).toList();
        return new CrewMemberResponse(c.getId(), c.getName(), c.getRole(), c.getExperienceLevel(), moduleIds);
    }
}
