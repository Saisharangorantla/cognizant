package com.cognizant.ems.service;

import com.cognizant.ems.model.Department;
import com.cognizant.ems.model.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Exercise 10 — Batch Processing with Hibernate.
 *
 * Demonstrates bulk-inserting a large number of Employee rows efficiently.
 * The batching behavior itself is configured declaratively in
 * application.properties:
 *
 *   spring.jpa.properties.hibernate.jdbc.batch_size=30
 *   spring.jpa.properties.hibernate.order_inserts=true
 *   spring.jpa.properties.hibernate.order_updates=true
 *   spring.jpa.properties.hibernate.batch_versioned_data=true
 *
 * With batch_size=30, Hibernate groups up to 30 INSERT statements into a
 * single JDBC batch (one network round-trip) instead of sending each
 * INSERT individually — a major throughput improvement for bulk loads.
 *
 * order_inserts/order_updates group statements by entity type so the
 * batches stay homogeneous (required for JDBC batching to kick in at
 * all when multiple entity types are saved in the same transaction).
 *
 * batch_versioned_data=true allows entities using @Version (optimistic
 * locking — see Employee.java, Exercise 10) to still be batched; without
 * this flag Hibernate falls back to one-by-one statements for versioned
 * entities on some JDBC drivers.
 *
 * IMPORTANT: periodically flush() + clear() the persistence context
 * during a large bulk insert loop. Without this, Hibernate keeps every
 * managed entity in memory for the whole transaction (defeating the
 * purpose of batching and risking OutOfMemoryError on large datasets).
 */
@Service
public class BatchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchService.class);
    private static final int BATCH_SIZE = 30;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Bulk-inserts the given employees in batches of BATCH_SIZE,
     * flushing and clearing the persistence context after each batch.
     */
    @Transactional
    public void bulkInsertEmployees(List<Employee> employees) {
        LOGGER.info("Start bulkInsertEmployees: count={}", employees.size());

        for (int i = 0; i < employees.size(); i++) {
            entityManager.persist(employees.get(i));

            if (i > 0 && i % BATCH_SIZE == 0) {
                LOGGER.debug("Flushing batch at index {}", i);
                entityManager.flush();
                entityManager.clear();
            }
        }

        // Flush any remaining entities that didn't fill a full batch.
        entityManager.flush();
        entityManager.clear();

        LOGGER.info("End bulkInsertEmployees");
    }

    /**
     * Convenience method generating N sample employees under the given
     * department, useful for demonstrating/benchmarking batch inserts.
     */
    @Transactional
    public void generateSampleEmployees(Department department, int count) {
        LOGGER.info("Start generateSampleEmployees: count={}", count);

        // Re-attach a managed Department instance within this transaction.
        Department managedDept = entityManager.merge(department);

        for (int i = 1; i <= count; i++) {
            Employee employee = new Employee();
            employee.setName("Batch Employee " + i);
            employee.setEmail("batch.employee" + i + "@cognizant.com");
            employee.setDepartment(managedDept);
            entityManager.persist(employee);

            if (i % BATCH_SIZE == 0) {
                LOGGER.debug("Flushing batch at index {}", i);
                entityManager.flush();
                entityManager.clear();
                managedDept = entityManager.merge(managedDept); // re-attach after clear()
            }
        }

        entityManager.flush();
        entityManager.clear();
        LOGGER.info("End generateSampleEmployees");
    }
}
