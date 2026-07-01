package com.cognizant.ems.controller;

import com.cognizant.ems.dto.EmployeeDepartmentDTO;
import com.cognizant.ems.dto.EmployeeSummary;
import com.cognizant.ems.model.Department;
import com.cognizant.ems.model.Employee;
import com.cognizant.ems.repository.DepartmentRepository;
import com.cognizant.ems.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing CRUD, search, pagination, and projection
 * endpoints for Employee.
 *
 * Exercise 4 — Implementing CRUD Operations.
 * Exercise 5 — Defining Query Methods (exposed via /search).
 * Exercise 6 — Implementing Pagination and Sorting.
 * Exercise 8 — Creating Projections.
 *
 * Endpoints:
 *   GET    /api/employees                         - paginated + sorted list (Exercise 6)
 *   GET    /api/employees/{id}                     - get one employee
 *   POST   /api/employees                          - create an employee
 *   PUT    /api/employees/{id}                      - update an employee
 *   DELETE /api/employees/{id}                      - delete an employee
 *   GET    /api/employees/search?name=&department=&emailDomain= - query methods (Exercise 5)
 *   GET    /api/employees/summary                  - interface-based projection (Exercise 8)
 *   GET    /api/employees/with-department           - class-based DTO projection (Exercise 8)
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    // ── Exercise 4: Basic CRUD ───────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee createEmployee(@RequestBody Employee employee) {
        employee.setId(null);
        // Re-attach a managed Department if the client only sent its id
        if (employee.getDepartment() != null && employee.getDepartment().getId() != null) {
            Department department = departmentRepository.findById(employee.getDepartment().getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Department not found: " + employee.getDepartment().getId()));
            employee.setDepartment(department);
        }
        return employeeRepository.save(employee);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                     @RequestBody Employee updated) {
        return employeeRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    existing.setEmail(updated.getEmail());
                    if (updated.getDepartment() != null && updated.getDepartment().getId() != null) {
                        departmentRepository.findById(updated.getDepartment().getId())
                                .ifPresent(existing::setDepartment);
                    }
                    return ResponseEntity.ok(employeeRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        employeeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ── Exercise 6: Pagination + Sorting ─────────────────────────────────────────

    /**
     * Example: GET /api/employees?page=0&size=10&sortBy=name&direction=asc
     */
    @GetMapping
    public Page<Employee> getAllEmployeesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findAll(pageable);
    }

    // ── Exercise 5: Query Methods ─────────────────────────────────────────────────

    /**
     * Example: GET /api/employees/search?name=ali
     *          GET /api/employees/search?departmentId=1
     *          GET /api/employees/search?emailDomain=cognizant.com
     * Only one parameter is expected per call in this simple example.
     */
    @GetMapping("/search")
    public List<Employee> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String emailDomain) {

        if (name != null) {
            return employeeRepository.findByNameContainingIgnoreCase(name);
        }
        if (departmentId != null) {
            return employeeRepository.findByDepartmentId(departmentId);
        }
        if (emailDomain != null) {
            return employeeRepository.findByEmailEndingWith(emailDomain);
        }
        return employeeRepository.findAll();
    }

    // ── Exercise 8: Projections ──────────────────────────────────────────────────

    /** Interface-based projection — lightweight id/name/email view. */
    @GetMapping("/summary")
    public List<EmployeeSummary> getEmployeeSummaries() {
        return employeeRepository.findAllProjectedBy();
    }

    /** Class-based DTO projection — built via JPQL constructor expression. */
    @GetMapping("/with-department")
    public List<EmployeeDepartmentDTO> getEmployeesWithDepartment() {
        return employeeRepository.findAllEmployeeDepartmentDTOs();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
