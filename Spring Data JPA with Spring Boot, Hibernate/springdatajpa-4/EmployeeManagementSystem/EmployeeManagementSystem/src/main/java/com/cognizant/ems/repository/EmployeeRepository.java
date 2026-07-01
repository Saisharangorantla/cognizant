package com.cognizant.ems.repository;

import com.cognizant.ems.dto.EmployeeDepartmentDTO;
import com.cognizant.ems.dto.EmployeeSummary;
import com.cognizant.ems.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity.
 * Exercise 3 — Creating Repositories (base CRUD via JpaRepository).
 * Exercise 5 — Defining Query Methods (derived methods + @Query + Named Queries).
 * Exercise 6 — Pagination and Sorting.
 * Exercise 8 — Projections (interface-based and class-based).
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // ── Exercise 5: Derived query methods (keyword-based) ──────────────────────

    /** Employees whose name contains the given text, case-insensitive. */
    List<Employee> findByNameContainingIgnoreCase(String name);

    /** Employees belonging to a department, by department id. */
    List<Employee> findByDepartmentId(Long departmentId);

    /** Employees whose email ends with the given domain suffix. */
    List<Employee> findByEmailEndingWith(String domainSuffix);

    // ── Exercise 5: Custom query via @Query (JPQL) ──────────────────────────────

    /**
     * Equivalent to findByDepartmentId, written explicitly with @Query to
     * demonstrate the annotation-based approach alongside derived methods.
     */
    @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId")
    List<Employee> searchByDepartmentId(@Param("departmentId") Long departmentId);

    /** Native SQL variant of the same query, for comparison. */
    @Query(value = "SELECT * FROM employee WHERE department_id = :departmentId", nativeQuery = true)
    List<Employee> searchByDepartmentIdNative(@Param("departmentId") Long departmentId);

    // ── Exercise 5: Named Queries (defined via @NamedQuery on Employee.java) ───
    // Spring Data automatically picks up a NamedQuery if its name matches
    // "<EntityName>.<methodName>" — e.g. "Employee.findByDepartmentName" below
    // is resolved from the @NamedQuery on the Employee entity, NOT from a
    // derived/@Query definition here.
    List<Employee> findByDepartmentName(String departmentName);

    // ── Exercise 6: Pagination & Sorting ─────────────────────────────────────────

    /**
     * Returns a Page of all employees. Pageable carries page number, page
     * size, AND sort instructions together (e.g. PageRequest.of(0, 10,
     * Sort.by("name").ascending())), so pagination and sorting are combined
     * in a single call from the caller's side.
     */
    Page<Employee> findAll(Pageable pageable);

    /** Paged + sorted search scoped to a single department. */
    Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

    // ── Exercise 8: Projections ──────────────────────────────────────────────────

    /**
     * Interface-based projection — Spring Data builds a lightweight proxy
     * over only the selected columns (see dto/EmployeeSummary.java).
     */
    List<EmployeeSummary> findByDepartmentId(Long departmentId, Class<EmployeeSummary> type);

    /** Same projection, returned for ALL employees. */
    List<EmployeeSummary> findAllProjectedBy();

    /**
     * Class-based projection via JPQL constructor expression — Hibernate
     * invokes EmployeeDepartmentDTO's constructor directly for every row.
     */
    @Query("SELECT new com.cognizant.ems.dto.EmployeeDepartmentDTO(e.id, e.name, d.name) "
            + "FROM Employee e JOIN e.department d")
    List<EmployeeDepartmentDTO> findAllEmployeeDepartmentDTOs();
}
