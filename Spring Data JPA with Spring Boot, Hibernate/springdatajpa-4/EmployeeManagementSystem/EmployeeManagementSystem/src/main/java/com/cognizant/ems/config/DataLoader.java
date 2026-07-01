package com.cognizant.ems.config;

import com.cognizant.ems.model.Department;
import com.cognizant.ems.model.Employee;
import com.cognizant.ems.repository.DepartmentRepository;
import com.cognizant.ems.repository.EmployeeRepository;
import com.cognizant.ems.service.BatchService;
import com.cognizant.ems.service.ReportingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Seeds sample Department/Employee data on application startup, and
 * exercises a few of the hands-on features (Named Query lookup, batch
 * insert, secondary datasource) so their generated SQL/log output is
 * visible immediately in the console without needing to call the REST
 * API first.
 *
 * Touches: Exercise 5 (Named Queries), Exercise 9 (secondary datasource),
 * Exercise 10 (batch processing).
 */
@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoader.class);

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final BatchService batchService;
    private final ReportingService reportingService;

    @PersistenceContext
    private EntityManager entityManager;

    public DataLoader(DepartmentRepository departmentRepository,
                       EmployeeRepository employeeRepository,
                       BatchService batchService,
                       ReportingService reportingService) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
        this.batchService = batchService;
        this.reportingService = reportingService;
    }

    @Override
    public void run(String... args) {
        LOGGER.info("=== DataLoader: seeding sample data ===");

        Department engineering = departmentRepository.save(new Department("Engineering"));
        Department hr = departmentRepository.save(new Department("Human Resources"));
        Department finance = departmentRepository.save(new Department("Finance"));

        employeeRepository.save(new Employee("Alice Johnson", "alice.johnson@cognizant.com", engineering));
        employeeRepository.save(new Employee("Bob Smith", "bob.smith@cognizant.com", engineering));
        employeeRepository.save(new Employee("Carol White", "carol.white@gmail.com", hr));
        employeeRepository.save(new Employee("David Brown", "david.brown@cognizant.com", finance));

        LOGGER.info("Seed data created: {} departments, {} employees",
                departmentRepository.count(), employeeRepository.count());

        // ── Exercise 5: Named Query demonstration ───────────────────────────────
        List<Employee> engineeringEmployees = entityManager
                .createNamedQuery("Employee.findByDepartmentName", Employee.class)
                .setParameter("departmentName", "Engineering")
                .getResultList();
        LOGGER.debug("Named Query 'Employee.findByDepartmentName' result: {}", engineeringEmployees);

        List<Employee> cognizantEmployees = entityManager
                .createNamedQuery("Employee.findByEmailDomain", Employee.class)
                .setParameter("emailPattern", "%@cognizant.com")
                .getResultList();
        LOGGER.debug("Named Query 'Employee.findByEmailDomain' result count: {}", cognizantEmployees.size());

        // ── Exercise 9: Secondary datasource demonstration ──────────────────────
        reportingService.getReportingDatabaseEcho();

        // ── Exercise 10: Batch processing demonstration ─────────────────────────
        batchService.generateSampleEmployees(engineering, 50);
        LOGGER.info("After batch insert, total employees: {}", employeeRepository.count());

        LOGGER.info("=== DataLoader: seeding complete ===");
    }
}
