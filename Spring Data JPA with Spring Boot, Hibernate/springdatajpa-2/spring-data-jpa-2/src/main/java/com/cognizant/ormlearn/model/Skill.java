package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import java.util.Set;

/**
 * Entity mapping for the 'skill' table.
 * Used in Hands-on 3, 6.
 */
@Entity
@Table(name = "skill")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sk_id")
    private int id;

    @Column(name = "sk_name")
    private String name;

    /**
     * Hands-on 6: Many-to-Many (inverse side).
     * mappedBy refers to 'skillList' defined in Employee.
     */
    @ManyToMany(mappedBy = "skillList")
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
        return "Skill{id=" + id + ", name='" + name + "'}";
    }
}
