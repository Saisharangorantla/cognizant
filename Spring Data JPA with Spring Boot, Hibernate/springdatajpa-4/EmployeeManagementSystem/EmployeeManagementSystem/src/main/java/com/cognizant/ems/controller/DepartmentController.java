package com.cognizant.ems.controller;

import com.cognizant.ems.model.Department;
import com.cognizant.ems.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller exposing CRUD endpoints for Department.
 * Exercise 4 — Implementing CRUD Operations.
 *
 * Endpoints:
 *   GET    /api/departments        - list all departments
 *   GET    /api/departments/{id}   - get one department
 *   POST   /api/departments        - create a department
 *   PUT    /api/departments/{id}   - update a department
 *   DELETE /api/departments/{id}   - delete a department
 */
@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentRepository departmentRepository;

    @GetMapping
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getDepartmentById(@PathVariable Long id) {
        return departmentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Department createDepartment(@RequestBody Department department) {
        // id is auto-generated; ignore any client-supplied id on create
        department.setId(null);
        return departmentRepository.save(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> updateDepartment(@PathVariable Long id,
                                                         @RequestBody Department updated) {
        return departmentRepository.findById(id)
                .map(existing -> {
                    existing.setName(updated.getName());
                    return ResponseEntity.ok(departmentRepository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        departmentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
