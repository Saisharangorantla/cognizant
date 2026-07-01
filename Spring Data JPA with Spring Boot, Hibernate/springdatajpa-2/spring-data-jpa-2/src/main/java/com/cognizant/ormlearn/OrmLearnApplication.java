package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.model.Department;
import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.model.Skill;
import com.cognizant.ormlearn.model.Stock;
import com.cognizant.ormlearn.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Main Spring Boot application class.
 *
 * All six hands-on test methods are here.  To run a specific hands-on,
 * un-comment the relevant method call(s) inside main() and comment the rest.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * HO-1  testCountryContaining / testCountryContainingSorted / testCountryStartsWith
 * HO-2  testFacebookStockSep2019 / testGoogleStocksAbove1250 /
 *        testTop3ByVolume / testNetflixLowest3
 * HO-3  (no runtime test — tables & beans are the deliverable)
 * HO-4  testGetEmployee / testAddEmployee / testUpdateEmployee
 * HO-5  testGetDepartment
 * HO-6  testGetEmployee (now also logs skills) / testAddSkillToEmployee
 * ─────────────────────────────────────────────────────────────────────────────
 */
@SpringBootApplication
public class OrmLearnApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrmLearnApplication.class);

    // ── Static service references (populated from context in main) ────────────
    private static CountryService    countryService;
    private static StockService      stockService;
    private static EmployeeService   employeeService;
    private static DepartmentService departmentService;
    private static SkillService      skillService;

    // ══════════════════════════════════════════════════════════════════════════
    //  ENTRY POINT
    // ══════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(OrmLearnApplication.class, args);

        countryService    = context.getBean(CountryService.class);
        stockService      = context.getBean(StockService.class);
        employeeService   = context.getBean(EmployeeService.class);
        departmentService = context.getBean(DepartmentService.class);
        skillService      = context.getBean(SkillService.class);

        // ── Hands-on 1 ───────────────────────────────────────────────────────
        testCountryContaining();
        testCountryContainingSorted();
        testCountryStartsWith();

        // ── Hands-on 2 ───────────────────────────────────────────────────────
        testFacebookStockSep2019();
        testGoogleStocksAbove1250();
        testTop3ByVolume();
        testNetflixLowest3();

        // ── Hands-on 4 ───────────────────────────────────────────────────────
        testGetEmployee();
        // testAddEmployee();     // un-comment to insert a new employee
        // testUpdateEmployee();  // un-comment to update an employee's department

        // ── Hands-on 5 ───────────────────────────────────────────────────────
        testGetDepartment();

        // ── Hands-on 6 ───────────────────────────────────────────────────────
        // testAddSkillToEmployee(); // un-comment to add a skill to an employee
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HANDS-ON 1 — Country Query Methods
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-1 Q1: Countries whose name contains "ou".
     * Expected: Bouvet Island, Djibouti, Guadeloupe, South Georgia…, Luxembourg,
     *           South Sudan, French Southern Territories,
     *           United States Minor Outlying Islands, South Africa
     */
    private static void testCountryContaining() {
        LOGGER.info("=== HO-1 Q1: Countries containing 'ou' ===");
        List<Country> countries = countryService.getAllCountriesContaining("ou");
        countries.forEach(c -> LOGGER.debug("{} {}", c.getCode(), c.getName()));
    }

    /**
     * HO-1 Q2: Same search but sorted A→Z.
     */
    private static void testCountryContainingSorted() {
        LOGGER.info("=== HO-1 Q2: Countries containing 'ou' sorted ASC ===");
        List<Country> countries = countryService.getAllCountriesContainingSorted("ou");
        countries.forEach(c -> LOGGER.debug("{} {}", c.getCode(), c.getName()));
    }

    /**
     * HO-1 Q3: Countries starting with "Z".
     * Expected: ZM Zambia, ZW Zimbabwe
     */
    private static void testCountryStartsWith() {
        LOGGER.info("=== HO-1 Q3: Countries starting with 'Z' ===");
        List<Country> countries = countryService.getAllCountriesStartingWith("Z");
        countries.forEach(c -> LOGGER.debug("{} {}", c.getCode(), c.getName()));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HANDS-ON 2 — Stock Query Methods
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-2 Q1: Facebook stocks in September 2019.
     */
    private static void testFacebookStockSep2019() {
        LOGGER.info("=== HO-2 Q1: Facebook stocks in Sep 2019 ===");
        List<Stock> stocks = stockService.getFacebookStockSep2019();
        stocks.forEach(s -> LOGGER.debug(
                "{} | {} | open={} | close={} | vol={}",
                s.getCode(), s.getDate(), s.getOpen(), s.getClose(), s.getVolume()));
    }

    /**
     * HO-2 Q2: Google stocks where close > 1250.
     */
    private static void testGoogleStocksAbove1250() {
        LOGGER.info("=== HO-2 Q2: Google stocks where close > 1250 ===");
        List<Stock> stocks = stockService.getGoogleStocksAbove1250();
        stocks.forEach(s -> LOGGER.debug(
                "{} | {} | open={} | close={} | vol={}",
                s.getCode(), s.getDate(), s.getOpen(), s.getClose(), s.getVolume()));
    }

    /**
     * HO-2 Q3: Top 3 dates with highest volume.
     * Expected: FB 2019-01-31 (77M), FB 2018-10-31 (60M), FB 2018-12-19 (57M)
     */
    private static void testTop3ByVolume() {
        LOGGER.info("=== HO-2 Q3: Top 3 highest volume dates ===");
        List<Stock> stocks = stockService.getTop3ByVolume();
        stocks.forEach(s -> LOGGER.debug(
                "{} | {} | vol={}", s.getCode(), s.getDate(), s.getVolume()));
    }

    /**
     * HO-2 Q4: Netflix 3 dates with lowest closing price.
     * Expected: NFLX 2018-12-24 (233.88), 2018-12-21 (246.39), 2018-12-26 (253.67)
     */
    private static void testNetflixLowest3() {
        LOGGER.info("=== HO-2 Q4: Netflix 3 lowest closing price dates ===");
        List<Stock> stocks = stockService.getNetflixLowest3();
        stocks.forEach(s -> LOGGER.debug(
                "{} | {} | close={}", s.getCode(), s.getDate(), s.getClose()));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HANDS-ON 4 — ManyToOne: Employee ↔ Department
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-4: Fetch employee by id and display associated department.
     * Also covers HO-6 skill display (skills are EAGER-loaded).
     */
    private static void testGetEmployee() {
        LOGGER.info("=== HO-4/HO-6: Get Employee (id=1) ===");
        Employee employee = employeeService.get(1);
        LOGGER.debug("Employee:   {}", employee);
        LOGGER.debug("Department: {}", employee.getDepartment());
        LOGGER.debug("Skills:     {}", employee.getSkillList());  // HO-6 line
    }

    /**
     * HO-4: Add a new employee assigned to department id=1.
     * Hibernate will INSERT the employee and auto-populate the id.
     */
    private static void testAddEmployee() {
        LOGGER.info("=== HO-4: Add Employee ===");

        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setSalary(75000.00);
        employee.setPermanent(true);
        employee.setDateOfBirth(new Date()); // today as placeholder

        // Fetch department so Hibernate can set the FK
        Department department = departmentService.get(1);
        employee.setDepartment(department);

        employeeService.save(employee);
        LOGGER.debug("Saved Employee: {}", employee);
    }

    /**
     * HO-4: Update an existing employee's department.
     * Fetches employee id=1 and changes its department to id=2.
     */
    private static void testUpdateEmployee() {
        LOGGER.info("=== HO-4: Update Employee Department ===");

        Employee employee = employeeService.get(1);
        Department newDepartment = departmentService.get(2);
        employee.setDepartment(newDepartment);

        employeeService.save(employee);
        LOGGER.debug("Updated Employee: {}", employee);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HANDS-ON 5 — OneToMany: Department → Employees
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-5: Fetch a department and display all its employees.
     * Requires @OneToMany(fetch=FetchType.EAGER) on Department.employeeList,
     * otherwise a LazyInitializationException is thrown.
     * Use department id that has more than one employee (e.g., 1).
     */
    private static void testGetDepartment() {
        LOGGER.info("=== HO-5: Get Department (id=1) with Employees ===");
        Department department = departmentService.get(1);
        LOGGER.debug("Department:    {}", department);
        Set<Employee> employees = department.getEmployeeList();
        LOGGER.debug("Employee list: {}", employees);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  HANDS-ON 6 — ManyToMany: Employee ↔ Skill
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-6: Add a skill to an employee.
     * Change employeeId / skillId to a pair that does NOT already exist
     * in the employee_skill join table to avoid duplicate-key errors.
     */
    private static void testAddSkillToEmployee() {
        LOGGER.info("=== HO-6: Add Skill to Employee ===");

        int employeeId = 1;  // change as needed
        int skillId    = 3;  // change as needed — must not already be linked

        Employee employee = employeeService.get(employeeId);
        Skill    skill    = skillService.get(skillId);

        Set<Skill> skillList = employee.getSkillList();
        skillList.add(skill);
        employee.setSkillList(skillList);

        employeeService.save(employee);
        LOGGER.debug("Employee after skill add: {}", employee);
        LOGGER.debug("Skills: {}", employee.getSkillList());
    }
}
