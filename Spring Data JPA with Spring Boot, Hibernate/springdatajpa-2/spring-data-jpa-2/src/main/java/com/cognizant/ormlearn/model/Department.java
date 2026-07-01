package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Entity mapping for the 'department' table.
 * Used in Hands-on 3, 4, 5.
 */
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dp_id")
    private int id;

    @Column(name = "dp_name")
    private String name;

    /**
     * Hands-on 5: One-to-Many relationship.
     * mappedBy refers to the 'department' field in Employee.
     * FetchType.EAGER ensures employees are loaded with the department.
     */
    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Employee> employeeList;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Employee> getEmployeeList() { return employeeList; }
    public void setEmployeeList(Set<Employee> employeeList) { this.employeeList = employeeList; }

    @Override
    public String toString() {
        return "Department{id=" + id + ", name='" + name + "'}";
    }
}
