package com.ninjaone.dundie_awards.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Audited
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Builder
@Entity
@Table(name = "activities",
        indexes = @Index(name = "ix_activity_occurred_at", columnList = "occurred_at")
)
public class ActivityEntity extends AuditableEntity {
    @ToString.Include
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @Column(name = "event")
    private String event;
}
