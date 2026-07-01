package com.cognizant.ormlearn;

import com.cognizant.ormlearn.model.Attempt;
import com.cognizant.ormlearn.model.AttemptOption;
import com.cognizant.ormlearn.model.AttemptQuestion;
import com.cognizant.ormlearn.model.Country;
import com.cognizant.ormlearn.model.Department;
import com.cognizant.ormlearn.model.Employee;
import com.cognizant.ormlearn.model.Product;
import com.cognizant.ormlearn.model.ProductSearchCriteria;
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
 * Combines hands-on exercises from BOTH training documents:
 *   springdata2.pdf — Query Methods + O/R Mapping
 *   springdata3.pdf — HQL / JPQL, fetch optimization, Native Query, Criteria Query
 *
 * To run a specific hands-on, un-comment the relevant method call(s)
 * inside main() and comment the rest.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * springdata2.pdf
 *   HO-1  testCountryContaining / testCountryContainingSorted / testCountryStartsWith
 *   HO-2  testFacebookStockSep2019 / testGoogleStocksAbove1250 /
 *          testTop3ByVolume / testNetflixLowest3
 *   HO-3  (no runtime test — tables & beans are the deliverable)
 *   HO-4  testGetEmployee / testAddEmployee / testUpdateEmployee
 *   HO-5  testGetDepartment
 *   HO-6  testGetEmployee (now also logs skills) / testAddSkillToEmployee
 *
 * springdata3.pdf
 *   HO-1  (conceptual — no runtime test)
 *   HO-2  testGetAllPermanentEmployees  (optimized single-query HQL fetch join)
 *   HO-3  testGetQuizAttempt            (HQL fetch join across 6 quiz tables)
 *   HO-4  testGetAverageSalary / testGetAverageSalaryByDepartment
 *   HO-5  testGetAllEmployeesNative     (Native Query)
 *   HO-6  testCriteriaQuerySearch       (dynamic Criteria Query)
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
    private static AttemptService    attemptService;
    private static ProductService    productService;

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
        attemptService    = context.getBean(AttemptService.class);
        productService    = context.getBean(ProductService.class);

        // ── springdata2.pdf — Hands-on 1 ────────────────────────────────────────
        testCountryContaining();
        testCountryContainingSorted();
        testCountryStartsWith();

        // ── springdata2.pdf — Hands-on 2 ────────────────────────────────────────
        testFacebookStockSep2019();
        testGoogleStocksAbove1250();
        testTop3ByVolume();
        testNetflixLowest3();

        // ── springdata2.pdf — Hands-on 4 ────────────────────────────────────────
        testGetEmployee();
        // testAddEmployee();     // un-comment to insert a new employee
        // testUpdateEmployee();  // un-comment to update an employee's department

        // ── springdata2.pdf — Hands-on 5 ────────────────────────────────────────
        testGetDepartment();

        // ── springdata2.pdf — Hands-on 6 ────────────────────────────────────────
        // testAddSkillToEmployee(); // un-comment to add a skill to an employee

        // ── springdata3.pdf — Hands-on 2: HQL + fetch optimization ─────────────
        testGetAllPermanentEmployees();

        // ── springdata3.pdf — Hands-on 3: Quiz attempt via HQL fetch join ──────
        testGetQuizAttempt();

        // ── springdata3.pdf — Hands-on 4: Average salary via HQL ───────────────
        testGetAverageSalary();
        testGetAverageSalaryByDepartment();

        // ── springdata3.pdf — Hands-on 5: Native Query ──────────────────────────
        testGetAllEmployeesNative();

        // ── springdata3.pdf — Hands-on 6: Criteria Query ────────────────────────
        testCriteriaQuerySearch();
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

    // ══════════════════════════════════════════════════════════════════════════
    //  springdata3.pdf — HANDS-ON 2: HQL — Permanent Employees (optimized fetch)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-2: Get all permanent employees using the optimized HQL fetch-join
     * query (department + skillList populated in a single SQL statement).
     * Compare the generated SQL in the logs against the multi-query
     * version described in the PDF before the 'fetch' keyword was added.
     */
    private static void testGetAllPermanentEmployees() {
        LOGGER.info("=== springdata3 HO-2: Get All Permanent Employees (HQL fetch join) ===");
        List<Employee> employees = employeeService.getAllPermanentEmployees();
        LOGGER.debug("Permanent Employees:{}", employees);
        employees.forEach(e -> LOGGER.debug("Skills:{}", e.getSkillList()));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  springdata3.pdf — HANDS-ON 3: Quiz Attempt Details (HQL fetch join)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-3: Fetch a user's quiz attempt with full details — questions,
     * each question's options, and which option(s) the user selected —
     * via a single HQL fetch-join query joining:
     *   user → attempt → attempt_question → question → attempt_option → options
     *
     * Output format mirrors the PDF example:
     *   <question text>
     *      <option number>) <option text> <option score> <selected true/false>
     */
    private static void testGetQuizAttempt() {
        LOGGER.info("=== springdata3 HO-3: Get Quiz Attempt Details ===");

        int userId = 1;
        int attemptId = 1;

        Attempt attempt = attemptService.getAttempt(userId, attemptId);
        if (attempt == null) {
            LOGGER.debug("No attempt found for userId={}, attemptId={}", userId, attemptId);
            return;
        }

        LOGGER.debug("User: {}", attempt.getUser().getName());
        LOGGER.debug("Attempted Date: {}", attempt.getDate());

        for (AttemptQuestion aq : attempt.getAttemptQuestionList()) {
            LOGGER.debug(aq.getQuestion().getText());

            int optionNumber = 1;
            for (AttemptOption ao : aq.getAttemptOptionList()) {
                LOGGER.debug("    {}) {} {} {}",
                        optionNumber++,
                        ao.getOption().getText(),
                        ao.getOption().getScore(),
                        ao.isSelected());
            }
        }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  springdata3.pdf — HANDS-ON 4: Average Salary (HQL aggregate function)
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-4 (unfiltered): Average salary across all employees.
     */
    private static void testGetAverageSalary() {
        LOGGER.info("=== springdata3 HO-4: Average Salary (all employees) ===");
        double avg = employeeService.getAverageSalary();
        LOGGER.debug("Average Salary: {}", avg);
    }

    /**
     * HO-4 (filtered): Average salary within a specific department.
     * Uses department id=1 (Engineering, per sample-data.sql).
     */
    private static void testGetAverageSalaryByDepartment() {
        LOGGER.info("=== springdata3 HO-4: Average Salary (department id=1) ===");
        double avg = employeeService.getAverageSalary(1);
        LOGGER.debug("Average Salary (dept=1): {}", avg);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  springdata3.pdf — HANDS-ON 5: Native Query
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-5: Get all employees using a raw SQL Native Query
     * (SELECT * FROM employee) instead of HQL.
     */
    private static void testGetAllEmployeesNative() {
        LOGGER.info("=== springdata3 HO-5: Get All Employees (Native Query) ===");
        List<Employee> employees = employeeService.getAllEmployeesNative();
        employees.forEach(e -> LOGGER.debug("{}", e));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  springdata3.pdf — HANDS-ON 6: Criteria Query
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * HO-6: Demonstrates the "Amazon laptop search" scenario using a
     * dynamic Criteria Query. Only the criteria fields that are set
     * (non-null) are applied as filters — mirroring a user who searched
     * "laptop" and then optionally narrowed by RAM, review rating, etc.
     *
     * Try commenting out individual criteria.setXxx(...) calls below and
     * re-running to see how the generated WHERE clause (and result set)
     * changes — that flexibility is exactly what Criteria Query provides
     * over a fixed HQL string.
     */
    private static void testCriteriaQuerySearch() {
        LOGGER.info("=== springdata3 HO-6: Criteria Query Product Search ===");

        ProductSearchCriteria criteria = new ProductSearchCriteria();
        criteria.setKeyword("laptop");      // free-text match on name
        criteria.setMinRamGb(16);           // RAM Size >= 16 GB
        criteria.setMinReview(4.0);         // Customer review >= 4.0
        // criteria.setOperatingSystem("Windows 11"); // try un-commenting more filters
        // criteria.setMaxWeightKg(1.5);
        // criteria.setCpu("Intel i7");

        List<Product> results = productService.search(criteria);
        LOGGER.debug("Matching Products ({}):", results.size());
        results.forEach(p -> LOGGER.debug("{}", p));
    }
}
