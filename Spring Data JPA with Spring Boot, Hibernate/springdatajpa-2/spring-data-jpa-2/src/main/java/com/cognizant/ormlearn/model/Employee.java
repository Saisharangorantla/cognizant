package com.cognizant.ormlearn.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Entity mapping for the 'employee' table.
 * Used in Hands-on 3, 4, 5, 6.
 */
@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "em_id")
    private int id;

    @Column(name = "em_name")
    private String name;

    @Column(name = "em_salary")
    private double salary;

    @Column(name = "em_permanent")
    private boolean permanent;

    @Column(name = "em_date_of_birth")
    private Date dateOfBirth;

    /**
     * Hands-on 4: Many-to-One relationship.
     * An employee belongs to one department.
     * FetchType.EAGER is the default for @ManyToOne (per JPA spec).
     */
    @ManyToOne
    @JoinColumn(name = "em_dp_id")
    private Department department;

    /**
     * Hands-on 6: Many-to-Many relationship (owning side).
     * employee_skill is the join table.
     * FetchType.EAGER ensures skills are loaded with the employee.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "employee_skill",
        joinColumns        = @JoinColumn(name = "es_em_id"),
        inverseJoinColumns = @JoinColumn(name = "es_sk_id")
    )
    private Set<Skill> skillList;

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public boolean isPermanent() { return permanent; }
    public void setPermanent(boolean permanent) { this.permanent = permanent; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Set<Skill> getSkillList() { return skillList; }
    public void setSkillList(Set<Skill> skillList) { this.skillList = skillList; }

    @Override
    public String toString() {
        return "Employee{id=" + id + ", name='" + name + "', salary=" + salary
                + ", permanent=" + permanent + ", dateOfBirth=" + dateOfBirth
                + ", department=" + department + "}";
    }
}
