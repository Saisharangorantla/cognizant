package com.cognizant.ems.dto;

/**
 * Exercise 8 — Class-based Projection (DTO).
 *
 * Unlike interface-based projections (EmployeeSummary), Spring Data JPA
 * populates this via a JPQL CONSTRUCTOR EXPRESSION, e.g.:
 *
 *   SELECT new com.cognizant.ems.dto.EmployeeDepartmentDTO(e.id, e.name, d.name)
 *   FROM Employee e JOIN e.department d
 *
 * Hibernate calls this exact constructor for every result row, giving a
 * concrete, serializable, immutable DTO — useful for REST API responses
 * where you don't want to expose the full JPA entity graph.
 *
 * The constructor's parameter order and types MUST match the constructor
 * expression in the query exactly.
 */
public class EmployeeDepartmentDTO {

    private final Long employeeId;
    private final String employeeName;
    private final String departmentName;

    public EmployeeDepartmentDTO(Long employeeId, String employeeName, String departmentName) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    @Override
    public String toString() {
        return "EmployeeDepartmentDTO{employeeId=" + employeeId
                + ", employeeName='" + employeeName + "'"
                + ", departmentName='" + departmentName + "'}";
    }
}
