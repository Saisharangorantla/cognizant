package com.cognizant.ems.model;

import com.cognizant.ems.audit.BaseAuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * Department entity.
 * Exercise 2 — Creating JPA Entities.
 * Exercise 7 — extends BaseAuditEntity for created/modified tracking.
 */
@Entity
@Table(name = "department")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employees") // avoid recursive toString with Employee
public class Department extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    /**
     * Exercise 2 — One-to-Many relationship between Department and Employee.
     * mappedBy refers to the 'department' field in Employee (owning side).
     * cascade = ALL + orphanRemoval lets you manage employees through the
     * department aggregate (e.g. department.getEmployees().add(employee)).
     */
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Employee> employees = new ArrayList<>();

    public Department(String name) {
        this.name = name;
    }
}
