# orm-learn — Spring Data JPA Hands-on Solutions

Complete Spring Boot project implementing **all 6 hands-on exercises** from the
Spring Data JPA training document:

| Hands-on | Topic |
|---|---|
| 1 | Query Methods on `country` table |
| 2 | Query Methods on `stock` table |
| 3 | Payroll schema + bean mapping (`Employee`, `Department`, `Skill`) |
| 4 | `@ManyToOne` — Employee ↔ Department |
| 5 | `@OneToMany` — Department → Employees |
| 6 | `@ManyToMany` — Employee ↔ Skill |

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

The project structure follows the standard Maven layout, which IntelliJ
understands natively:

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
        │   │   └── Skill.java
        │   ├── repository/
        │   │   ├── CountryRepository.java
        │   │   ├── StockRepository.java
        │   │   ├── EmployeeRepository.java
        │   │   ├── DepartmentRepository.java
        │   │   └── SkillRepository.java
        │   └── service/
        │       ├── CountryService.java
        │       ├── StockService.java
        │       ├── EmployeeService.java
        │       ├── DepartmentService.java
        │       └── SkillService.java
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

This creates the `ormlearn` database and all tables: `country`, `stock`,
`department`, `skill`, `employee`, `employee_skill`.

### 3.2 Load sample data (country / department / skill / employee)

```bash
mysql -u root -p ormlearn < src/main/resources/sample-data.sql
```

This gives you enough data to run Hands-on 1, 3, 4, 5, 6 immediately.

### 3.3 Load stock data (Hands-on 2)

The original PDF instructs you to use Excel + a `CONCATENATE` formula to
convert `stock-data.csv` into insert statements. This project includes a
Python helper that does the same thing without Excel:

```bash
python scripts/generate_stock_data.py stock-data.csv stock-data.sql
mysql -u root -p ormlearn < stock-data.sql
```

> Get `stock-data.csv` from the `spring-data-jpa-files` folder provided in
> your training platform (Facebook/Google/Netflix data, 18-Oct-2018 to
> 17-Oct-2019). If your CSV's column headers differ, adjust the
> `COLUMN_MAP` dictionary at the top of `generate_stock_data.py`.

### 3.4 Configure credentials

Edit `src/main/resources/application.properties` and set your MySQL
username/password:

```properties
spring.datasource.username=root
spring.datasource.password=root
```

---

## 4. Running the Project

### From IntelliJ
1. Open `OrmLearnApplication.java`
2. Right-click → **Run 'OrmLearnApplication.main()'**
3. By default, `main()` runs the Hands-on 1, 2, 4, and 5 test methods.
   Un-comment/comment lines inside `main()` to run specific test methods
   (e.g. `testAddEmployee()`, `testAddSkillToEmployee()`).
4. Observe results in the **Run** console — Hibernate SQL and result data
   are logged at DEBUG level.

### From command line
```bash
mvn spring-boot:run
```

---

## 5. Hands-on-by-Hands-on Summary

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
- Tables created via `schema.sql`: `employee`, `department`, `skill`,
  `employee_skill`
- Entities: `Employee.java`, `Department.java`, `Skill.java` (with
  `@Entity`, `@Table`, `@Id`, `@GeneratedValue(strategy = IDENTITY)`,
  `@Column`)
- Repositories: `EmployeeRepository`, `DepartmentRepository`,
  `SkillRepository`

### Hands-on 4 — `@ManyToOne` (Employee → Department)
- `Employee.department` mapped with `@ManyToOne @JoinColumn(name="em_dp_id")`
- `testGetEmployee()` — fetches employee + department (EAGER by JPA default)
- `testAddEmployee()` — inserts a new employee linked to a department
- `testUpdateEmployee()` — changes an employee's department

### Hands-on 5 — `@OneToMany` (Department → Employees)
- `Department.employeeList` mapped with
  `@OneToMany(mappedBy="department", fetch=FetchType.EAGER)`
- `testGetDepartment()` — fetches a department and its employee list
  (EAGER avoids `LazyInitializationException`)

### Hands-on 6 — `@ManyToMany` (Employee ↔ Skill)
- Owning side: `Employee.skillList` with
  `@ManyToMany(fetch=EAGER) @JoinTable(name="employee_skill", joinColumns=@JoinColumn(name="es_em_id"), inverseJoinColumns=@JoinColumn(name="es_sk_id"))`
- Inverse side: `Skill.employeeList` with `@ManyToMany(mappedBy="skillList")`
- `testGetEmployee()` also logs `employee.getSkillList()`
- `testAddSkillToEmployee()` — links an existing employee to a new skill

---

## 6. Notes

- `spring.jpa.hibernate.ddl-auto=update` is set so Hibernate will validate/
  create columns matching the entities on top of the `schema.sql` tables.
  If you prefer Hibernate to fully own schema creation, you can drop
  `schema.sql` and set `ddl-auto=create` instead — but the explicit SQL
  script is included so the FK constraints exactly match the original
  hands-on document.
- All service methods are `@Transactional` to keep lazy associations
  accessible within the method scope and to match the patterns shown in
  the original PDF.
- Logging is configured at `DEBUG` for `com.cognizant.ormlearn` and
  `org.hibernate.SQL` so you can observe the generated SQL queries,
  exactly as instructed in the hands-on document (e.g., observing the
  `LEFT OUTER JOIN` to `department` when fetching an employee).
