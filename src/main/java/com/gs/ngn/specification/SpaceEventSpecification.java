package com.gs.ngn.specification;

import com.gs.ngn.domainmodel.entity.SpaceEvent;
import com.gs.ngn.domainmodel.enums.SpaceEventType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class SpaceEventSpecification {

    private SpaceEventSpecification() {}

    public static Specification<SpaceEvent> hasType(SpaceEventType type) {
        return (root, query, cb) ->
            type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<SpaceEvent> hasDangerLevelAbove(Double minDanger) {
        return (root, query, cb) ->
            minDanger == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("dangerLevel"), minDanger);
    }

    public static Specification<SpaceEvent> occurredAfter(LocalDateTime from) {
        return (root, query, cb) ->
            from == null ? cb.conjunction() : cb.greaterThanOrEqualTo(root.get("eventDate"), from);
    }

    public static Specification<SpaceEvent> occurredBefore(LocalDateTime to) {
        return (root, query, cb) ->
            to == null ? cb.conjunction() : cb.lessThanOrEqualTo(root.get("eventDate"), to);
    }
}
