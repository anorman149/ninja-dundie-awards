package com.ninjaone.dundie_awards.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

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
@Table(name = "employees", indexes = {
        @Index(name = "ix_employee_organization_id", columnList = "organization_id")
})
public class EmployeeEntity extends AuditableEntity {
    @ToString.Include
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "dundie_awards")
    private Integer dundieAwards;

    @NotAudited
    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = CascadeType.MERGE)
    @JoinColumn(name = "organization_id", foreignKey = @ForeignKey(name = "fk_employees_organization_id"))
    private OrganizationEntity organization;
}