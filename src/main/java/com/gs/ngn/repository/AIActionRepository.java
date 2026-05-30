package com.gs.ngn.repository;

import com.gs.ngn.domainmodel.entity.AIAction;
import com.gs.ngn.domainmodel.enums.AIActionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIActionRepository extends JpaRepository<AIAction, Long> {
    List<AIAction> findByAlertId(Long alertId);
    Page<AIAction> findByExecuted(Boolean executed, Pageable pageable);
    List<AIAction> findByType(AIActionType type);
}
