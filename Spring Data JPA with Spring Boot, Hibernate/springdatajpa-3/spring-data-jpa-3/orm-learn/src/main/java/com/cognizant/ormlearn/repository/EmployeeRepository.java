package com.cognizant.ormlearn.repository;

import com.cognizant.ormlearn.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Employee entity.
 * Used in Hands-on 4, 5, 6 (springdata2.pdf) and
 * Hands-on 2, 4, 5 (springdata3.pdf — HQL / Native Query).
 * Basic CRUD operations are inherited from JpaRepository.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    /**
     * springdata3 HO-2 (initial, naive version):
     * Plain HQL — 'join' only links tables, it does NOT populate beans.
     * With EAGER fetch configured on Employee.skillList and
     * Department.employeeList, Hibernate fires 3 queries total
     * (1 for employees, then N+1 lazy/eager follow-ups for department
     * and skills). Kept here, commented, purely for reference/comparison —
     * see getAllPermanentEmployees() below for the optimal version.
     *
     * @Query(value = "SELECT e FROM Employee e WHERE e.permanent = 1")
     * List<Employee> getAllPermanentEmployees();
     */

    /**
     * springdata3 HO-2 (optimized, final version):
     * Uses 'left join fetch' on both department and skillList so the
     * ENTIRE object graph (employee + department + skills) is populated
     * in a SINGLE SQL query, instead of 3 separate queries.
     *
     * IMPORTANT: when using 'fetch', remove the EAGER fetch type from
     * Employee.skillList (@ManyToMany) and Department.employeeList
     * (@OneToMany) — otherwise Hibernate fires extra eager-fetch queries
     * on top of this one regardless. With LAZY default + explicit
     * 'fetch' here, exactly one query is executed.
     */
    @Query(value = "SELECT e FROM Employee e "
            + "left join fetch e.department d "
            + "left join fetch e.skillList "
            + "WHERE e.permanent = 1")
    List<Employee> getAllPermanentEmployees();

    /**
     * springdata3 HO-4 (unfiltered):
     * Average salary across ALL employees.
     */
    @Query(value = "SELECT AVG(e.salary) FROM Employee e")
    double getAverageSalary();

    /**
     * springdata3 HO-4 (filtered by department):
     * Average salary of employees within a given department.
     * Note how 'e.department.id' navigates the association in HQL,
     * and ':id' + @Param bind the method argument to the query parameter.
     */
    @Query(value = "SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :id")
    double getAverageSalary(@Param("id") int id);

    /**
     * springdata3 HO-5: Native Query.
     * Runs a direct SQL statement against the database instead of HQL.
     * nativeQuery = true tells Spring Data JPA to NOT translate this as HQL.
     * Native queries should be used sparingly since they reduce database
     * portability.
     */
    @Query(value = "SELECT * FROM employee", nativeQuery = true)
    List<Employee> getAllEmployeesNative();
}
