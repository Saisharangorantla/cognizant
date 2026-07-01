package com.cognizant.ems.repository;

import com.cognizant.ems.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for Department entity.
 * Exercise 3 — Creating Repositories.
 *
 * Extending JpaRepository<Department, Long> gives us, for free:
 *   save(), findById(), findAll(), deleteById(), count(), existsById(), etc.
 * — no implementation code required; Spring Data generates it at runtime.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /** Derived query method: lookup a department by its exact name. */
    Optional<Department> findByName(String name);
}
