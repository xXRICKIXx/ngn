package com.gs.ngn.domainmodel.valueobject;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AtmosphericCondition {
    @Column(nullable = false) private Double oxygenLevel;
    @Column(nullable = false) private Double temperature;
    @Column(nullable = false) private Double humidity;
    @Column(nullable = false) private Double pressure;
    @Column(nullable = false) private Double radiationLevel;
}
