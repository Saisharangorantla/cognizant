package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Department entity.
 * Used in Hands-on 4, 5.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}
