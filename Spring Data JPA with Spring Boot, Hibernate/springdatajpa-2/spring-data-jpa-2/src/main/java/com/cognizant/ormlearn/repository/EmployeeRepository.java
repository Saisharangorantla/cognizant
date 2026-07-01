package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Employee entity.
 * Used in Hands-on 4, 5, 6.
 * Basic CRUD operations are inherited from JpaRepository.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
