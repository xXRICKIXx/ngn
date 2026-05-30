package com.gs.ngn.specification;

import com.gs.ngn.domainmodel.entity.Module;
import com.gs.ngn.domainmodel.enums.ModuleType;
import org.springframework.data.jpa.domain.Specification;

public class ModuleSpecification {

    private ModuleSpecification() {}

    public static Specification<Module> hasType(ModuleType type) {
        return (root, query, cb) ->
            type == null ? cb.conjunction() : cb.equal(root.get("type"), type);
    }

    public static Specification<Module> isActive(Boolean active) {
        return (root, query, cb) ->
            active == null ? cb.conjunction() : cb.equal(root.get("active"), active);
    }

    public static Specification<Module> fromHabitat(Long habitatId) {
        return (root, query, cb) ->
            habitatId == null ? cb.conjunction() : cb.equal(root.get("habitat").get("id"), habitatId);
    }

    public static Specification<Module> isEssential() {
        return (root, query, cb) -> root.get("type").in(
            ModuleType.LIFE_SUPPORT, ModuleType.ENERGY, ModuleType.DEFENSE
        );
    }
}
