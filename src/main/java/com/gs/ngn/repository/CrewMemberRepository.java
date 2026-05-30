package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.CrewMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CrewMemberRepository extends JpaRepository<CrewMember, Long> {
    List<CrewMember> findByRole(String role);

    @Query("SELECT cm FROM CrewMember cm JOIN cm.modules m WHERE m.id = :moduleId")
    List<CrewMember> findByModuleId(Long moduleId);
}
