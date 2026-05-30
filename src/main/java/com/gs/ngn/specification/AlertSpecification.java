package com.gs.ngn.specification;

import com.gs.ngn.domainmodel.entity.Alert;
import com.gs.ngn.domainmodel.enums.AlertLevel;
import com.gs.ngn.domainmodel.enums.AlertType;
import org.springframework.data.jpa.domain.Specification;

public class AlertSpecification {

    private AlertSpecification() {}

    public static Specification<Alert> hasType(AlertType type) {
        return (root, query, cb) ->
            type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<Alert> hasLevel(AlertLevel level) {
        return (root, query, cb) ->
            level == null ? cb.conjunction() : cb.equal(root.get("level"), level);
    }

    public static Specification<Alert> isResolved(Boolean resolved) {
        return (root, query, cb) ->
            resolved == null ? cb.conjunction() : cb.equal(root.get("resolved"), resolved);
    }

    public static Specification<Alert> fromHabitat(Long habitatId) {
        return (root, query, cb) ->
            habitatId == null ? cb.conjunction() : cb.equal(root.get("habitat").get("id"), habitatId);
    }

    public static Specification<Alert> isCritical() {
        return hasLevel(AlertLevel.CRITICAL).and(isResolved(false));
    }
}
