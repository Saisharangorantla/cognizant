package com.cognizant.ems.model;

import com.cognizant.ems.audit.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 * Employee entity.
 * Exercise 2 — Creating JPA Entities.
 * Exercise 5 — Named Queries via @NamedQuery / @NamedQueries.
 * Exercise 7 — extends BaseAuditEntity for created/modified tracking.
 * Exercise 10 — Hibernate-specific annotations:
 *   @DynamicUpdate — generates UPDATE statements containing only the
 *                    columns that actually changed, instead of every
 *                    mapped column. Reduces unnecessary writes/locking
 *                    on wide tables where only one field changes.
 *   @DynamicInsert — likewise, omits null-valued columns from the
 *                    generated INSERT, relying on DB column defaults.
 *   @Version field below enables Hibernate's optimistic concurrency
 *   control: the 'version' column is checked/incremented on every
 *   update, and a stale write throws OptimisticLockException instead of
 *   silently overwriting a concurrent change.
 */
@Entity
@Table(name = "employee")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "department") // avoid recursive toString with Department
@DynamicUpdate
@DynamicInsert
@NamedQueries({
    /**
     * Exercise 5 — Named Query: all employees belonging to a department,
     * looked up by department name.
     * Usage: entityManager.createNamedQuery("Employee.findByDepartmentName", Employee.class)
     */
    @NamedQuery(
        name = "Employee.findByDepartmentName",
        query = "SELECT e FROM Employee e WHERE e.department.name = :departmentName"
    ),
    /**
     * Exercise 5 — Named Query: employees whose email matches a domain
     * (e.g. "%@cognizant.com").
     */
    @NamedQuery(
        name = "Employee.findByEmailDomain",
        query = "SELECT e FROM Employee e WHERE e.email LIKE :emailPattern"
    )
})
public class Employee extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Exercise 10 — Optimistic locking version column. Hibernate
     * increments this automatically on every UPDATE and includes it in
     * the WHERE clause, so a concurrent stale write fails fast with
     * OptimisticLockException rather than silently clobbering changes.
     */
    @Version
    private Long version;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Exercise 2 — Many-to-One relationship (owning side).
     * Each employee belongs to one department.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee(String name, String email, Department department) {
        this.name = name;
        this.email = email;
        this.department = department;
    }
}
