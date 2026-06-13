package com.ninjaone.dundie_awards.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;

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
@Table(name = "organizations",
        indexes = @Index(name = "ix_organization_name", columnList = "name"),
        uniqueConstraints = @UniqueConstraint(name = "ux_organization_name", columnNames = "name")
)
public class OrganizationEntity extends AuditableEntity {
    @ToString.Include
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @ToString.Include
    @Column(name = "name")
    private String name;
}
