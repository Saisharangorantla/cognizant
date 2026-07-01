# orm-learn — Spring Data JPA Hands-on Solutions

Complete Spring Boot project implementing **all hands-on exercises** from
both training documents:

**springdata2.pdf** — Query Methods & O/R Mapping

| Hands-on | Topic |
|---|---|
| 1 | Query Methods on `country` table |
| 2 | Query Methods on `stock` table |
| 3 | Payroll schema + bean mapping (`Employee`, `Department`, `Skill`) |
| 4 | `@ManyToOne` — Employee ↔ Department |
| 5 | `@OneToMany` — Department → Employees |
| 6 | `@ManyToMany` — Employee ↔ Skill |

**springdata3.pdf** — HQL/JPQL, Native Query, Criteria Query

| Hands-on | Topic |
|---|---|
| 1 | Introduction to HQL/JPQL (conceptual) |
| 2 | Permanent employees via HQL, optimized with `fetch` joins |
| 3 | Quiz attempt details via HQL fetch join across 6 tables |
| 4 | Average salary via HQL aggregate function (`AVG`) |
| 5 | Native Query (`SELECT * FROM employee`) |
| 6 | Criteria Query — dynamic product search filters |

Both documents build on the **same** `Employee`/`Department`/`Skill`
schema, so this remains a single unified project rather than separate
ones — splitting them would break the relationships hands-on 4/5/6 and
springdata3 HO-2/HO-4 depend on.

---

## 1. Prerequisites

- JDK 17+
- Maven 3.6+ (or use the IntelliJ-bundled Maven)
- MySQL 8.x server running locally
- IntelliJ IDEA (Community or Ultimate)

---

## 2. Importing into IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open**
2. Select the `orm-learn` folder (the one containing `pom.xml`)
3. IntelliJ auto-detects it as a **Maven project** — click **Trust Project**
4. Wait for Maven to download dependencies (bottom-right progress bar)
5. Ensure **Project SDK** is set to Java 17:
   `File → Project Structure → Project → SDK`

```
orm-learn/
├── pom.xml
├── README.md
├── scripts/
│   └── generate_stock_data.py
└── src/
    └── main/
        ├── java/com/cognizant/ormlearn/
        │   ├── OrmLearnApplication.java
        │   ├── model/
        │   │   ├── Country.java
        │   │   ├── Stock.java
        │   │   ├── Employee.java
        │   │   ├── Department.java
        │   │   ├── Skill.java
        │   │   ├── User.java                  ← quiz schema
        │   │   ├── Question.java               ← quiz schema
        │   │   ├── Option.java                 ← quiz schema
        │   │   ├── Attempt.java                ← quiz schema
        │   │   ├── AttemptQuestion.java         ← quiz schema
        │   │   ├── AttemptOption.java           ← quiz schema
        │   │   ├── Product.java                 ← criteria query
        │   │   └── ProductSearchCriteria.java   ← criteria query
        │   ├── repository/
        │   │   ├── CountryRepository.java
        │   │   ├── StockRepository.java
        │   │   ├── EmployeeRepository.java      (HQL + native query methods)
        │   │   ├── DepartmentRepository.java
        │   │   ├── SkillRepository.java
        │   │   ├── UserRepository.java
        │   │   ├── QuestionRepository.java
        │   │   ├── OptionRepository.java
        │   │   ├── AttemptRepository.java       (HQL fetch join across 6 tables)
        │   │   ├── AttemptQuestionRepository.java
        │   │   ├── AttemptOptionRepository.java
        │   │   ├── ProductRepository.java
        │   │   └── ProductSearchRepository.java (Criteria Query — CriteriaBuilder/Root/TypedQuery)
        │   └── service/
        │       ├── CountryService.java
        │       ├── StockService.java
        │       ├── EmployeeService.java
        │       ├── DepartmentService.java
        │       ├── SkillService.java
        │       ├── AttemptService.java
        │       └── ProductService.java
        └── resources/
            ├── application.properties
            ├── schema.sql
            └── sample-data.sql
```

---

## 3. Database Setup

### 3.1 Create schema and tables

```bash
mysql -u root -p < src/main/resources/schema.sql
```

This creates the `ormlearn` database and ALL tables across both
documents: `country`, `stock`, `department`, `skill`, `employee`,
`employee_skill`, plus the quiz schema (`user`, `question`, `options`,
`attempt`, `attempt_question`, `attempt_option`) and `product`.

### 3.2 Load sample data

```bash
mysql -u root -p ormlearn < src/main/resources/sample-data.sql
```

This includes country/department/skill/employee data, a fully-populated
quiz attempt (user id=1, attempt id=1, matching the exact 4-question
example from springdata3.pdf Hands-on 3), and 6 sample laptops for the
Criteria Query hands-on.

### 3.3 Load stock data (springdata2.pdf Hands-on 2)

```bash
python scripts/generate_stock_data.py stock-data.csv stock-data.sql
mysql -u root -p ormlearn < stock-data.sql
```

> Get `stock-data.csv` from the `spring-data-jpa-files` folder provided in
> your training platform.

### 3.4 Configure credentials

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

---

## 4. Running the Project

### From IntelliJ
1. Open `OrmLearnApplication.java`
2. Right-click → **Run 'OrmLearnApplication.main()'**
3. By default, `main()` runs most test methods across both documents.
   Comment/un-comment lines inside `main()` to isolate a specific
   hands-on (e.g., just `testGetQuizAttempt()` or
   `testCriteriaQuerySearch()`).
4. Watch the **Run** console — Hibernate SQL is logged at DEBUG level,
   so you can directly compare query counts (e.g., the 3-query vs.
   1-query versions described in springdata3.pdf HO-2).

### From command line
```bash
mvn spring-boot:run
```

---

## 5. springdata2.pdf — Hands-on Summary

### Hands-on 1 — Query Methods on Country (`CountryRepository`)
| Method | Purpose |
|---|---|
| `findByNameContaining(text)` | Search box matching (e.g. "ou") |
| `findByNameContainingOrderByNameAsc(text)` | Same, sorted A→Z |
| `findByNameStartingWith(prefix)` | Alphabet index click (e.g. "Z") |

### Hands-on 2 — Query Methods on Stock (`StockRepository`)
| Method | Purpose |
|---|---|
| `findByCodeAndDateBetween(code, start, end)` | FB stocks in Sep 2019 |
| `findByCodeAndCloseGreaterThan(code, price)` | GOOGL stocks close > 1250 |
| `findTop3ByOrderByVolumeDesc()` | Top 3 highest-volume dates |
| `findTop3ByCodeOrderByCloseAsc(code)` | NFLX 3 lowest-close dates |

### Hands-on 3 — Payroll schema & bean mapping
Tables, entities (`Employee`, `Department`, `Skill`) and repositories as
described in the PDF.

### Hands-on 4 — `@ManyToOne` (Employee → Department)
`testGetEmployee()` / `testAddEmployee()` / `testUpdateEmployee()`

### Hands-on 5 — `@OneToMany` (Department → Employees)
`testGetDepartment()` — relies on `Department.employeeList` staying
`FetchType.EAGER` (see note in `Department.java` explaining why this
EAGER setting was intentionally kept even after the springdata3.pdf
optimization discussion — that discussion targets a *different* query
path that no longer relies on it).

### Hands-on 6 — `@ManyToMany` (Employee ↔ Skill)
`testGetEmployee()` (skills logged) / `testAddSkillToEmployee()`

---

## 6. springdata3.pdf — Hands-on Summary

### Hands-on 1 — HQL/JPQL Introduction
Conceptual only; no runtime test method (see PDF page 2 for the
HQL-vs-JPQL comparison notes, reproduced as Javadoc comments where
relevant).

### Hands-on 2 — Get All Permanent Employees Using HQL
`EmployeeRepository.getAllPermanentEmployees()` demonstrates the full
progression described in the PDF:

1. A naive `WHERE e.permanent = 1` HQL string (kept as a commented
   reference in the repository) — combined with EAGER associations,
   this produces **3 separate SQL queries**.
2. The **optimized** final version:
   ```java
   @Query("SELECT e FROM Employee e "
        + "left join fetch e.department d "
        + "left join fetch e.skillList "
        + "WHERE e.permanent = 1")
   ```
   This produces a **single SQL query** with `LEFT OUTER JOIN`s to both
   `department` and `employee_skill`/`skill`.

   To make this work as intended, `Employee.skillList` was changed from
   `@ManyToMany(fetch = FetchType.EAGER)` to the JPA-default `LAZY` — so
   skills are only loaded when an HQL query explicitly asks for them via
   `fetch`, exactly matching the PDF's "Optimizing HQL Solution" section
   and its "IMPORTANT TAKEAWAY: join links the tables, fetch populates
   the beans" callout.

Test: `testGetAllPermanentEmployees()`

### Hands-on 3 — Fetch Quiz Attempt Details Using HQL
New schema added: `User`, `Question`, `Option`, `Attempt`,
`AttemptQuestion`, `AttemptOption` (entities), `AttemptRepository`,
`AttemptService`.

`AttemptRepository.getAttempt(userId, attemptId)` runs a single HQL query
joining, in the exact order specified by the PDF:
```
user → attempt → attempt_question → question → attempt_option → options
```
with `fetch` applied at every one-to-many hop so the entire nested object
graph is populated in one round trip.

`sample-data.sql` seeds exactly the 4-question attempt from the PDF's
worked example (HTML quiz questions), so running
`testGetQuizAttempt()` reproduces the same output structure:
```
What is the extension of the hyper text markup language file?
    1) .xhtm 0.0 false
    2) .ht 0.0 false
    3) .html 1.0 true
    4) .htmx 0.0 false
...
```

### Hands-on 4 — Get Average Salary Using HQL
`EmployeeRepository`:
```java
@Query("SELECT AVG(e.salary) FROM Employee e")
double getAverageSalary();

@Query("SELECT AVG(e.salary) FROM Employee e WHERE e.department.id = :id")
double getAverageSalary(@Param("id") int id);
```
Tests: `testGetAverageSalary()` / `testGetAverageSalaryByDepartment()`

### Hands-on 5 — Get All Employees Using Native Query
```java
@Query(value = "SELECT * FROM employee", nativeQuery = true)
List<Employee> getAllEmployeesNative();
```
Test: `testGetAllEmployeesNative()`

### Hands-on 6 — Criteria Query
Models the "Amazon laptop search" scenario from the PDF: a `Product`
entity plus a `ProductSearchCriteria` DTO holding **optional** filters
(review, hard disk, RAM, CPU speed, OS, weight, CPU). 

`ProductSearchRepository` (a plain `@Repository` class using
`EntityManager` directly, since Criteria Query isn't expressible as a
Spring Data derived/`@Query` method) builds the predicate list at
runtime using `CriteriaBuilder`, `CriteriaQuery`, `Root`, and
`TypedQuery` — only adding a `Predicate` for each filter the caller
actually set, then `AND`-combining whatever's present. This is the
direct solution to the PDF's question: *"what will be the where clause
... ? The where clause varies based on the criteria selected by the
user."*

Test: `testCriteriaQuerySearch()` — try commenting/un-commenting the
`criteria.setXxx(...)` calls to see the generated SQL's WHERE clause
change accordingly.

---

## 7. Notes

- `spring.jpa.hibernate.ddl-auto=update` lets Hibernate validate/create
  columns matching the entities on top of the `schema.sql` tables.
- All service methods are `@Transactional` to keep lazy associations
  accessible within method scope.
- Logging is at `DEBUG` for `com.cognizant.ormlearn` and
  `org.hibernate.SQL`, so generated SQL is visible in the console —
  useful for directly comparing query counts as described throughout
  springdata3.pdf (e.g. 3 queries → 1 query after adding `fetch`).
- The `user` table is quoted as `` `user` `` in both `schema.sql` and the
  `@Table` annotation, since `USER` is a reserved word in MySQL 8+.

