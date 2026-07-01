# EmployeeManagementSystem — Spring Data JPA Hands-on Solutions

Complete Spring Boot project implementing **all 10 exercises** from the
Employee Management System training document (springdata4.pdf).

| Exercise | Topic |
|---|---|
| 1 | Project setup — H2 database configuration |
| 2 | JPA entities — Employee, Department, one-to-many mapping |
| 3 | Repositories — EmployeeRepository, DepartmentRepository |
| 4 | CRUD operations via REST controllers |
| 5 | Query methods — derived methods, `@Query`, `@NamedQuery`/`@NamedQueries` |
| 6 | Pagination and sorting |
| 7 | Entity auditing — `@CreatedDate`, `@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy` |
| 8 | Projections — interface-based and class-based (DTO) |
| 9 | Customizing data source configuration — multiple data sources |
| 10 | Hibernate-specific features — `@DynamicUpdate`, `@DynamicInsert`, `@Version`, batch processing |

---

## 1. Prerequisites

- JDK 17+
- Maven 3.6+ (or the IntelliJ-bundled Maven)
- IntelliJ IDEA (Community or Ultimate)
- **No external database needed** — this project uses H2 in-memory, so it
  runs out of the box with zero setup.

---

## 2. Importing into IntelliJ IDEA

1. Open IntelliJ IDEA → **File → Open**
2. Select the `EmployeeManagementSystem` folder (the one containing `pom.xml`)
3. IntelliJ auto-detects it as a **Maven project** — click **Trust Project**
4. Wait for Maven to download dependencies
5. Ensure **Project SDK** is set to Java 17 and **Lombok plugin** is
   enabled (`File → Settings → Plugins → Lombok` — bundled by default in
   recent IntelliJ versions) with annotation processing turned on
   (`Settings → Build, Execution, Deployment → Compiler → Annotation
   Processors → Enable annotation processing`).

```
EmployeeManagementSystem/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/com/cognizant/ems/
        │   ├── EmployeeManagementSystemApplication.java
        │   ├── audit/
        │   │   └── BaseAuditEntity.java          ← Exercise 7
        │   ├── config/
        │   │   ├── AuditorAwareImpl.java          ← Exercise 7
        │   │   ├── DataSourceConfig.java          ← Exercise 9
        │   │   └── DataLoader.java                ← seeds data + demos
        │   ├── controller/
        │   │   ├── DepartmentController.java      ← Exercise 4
        │   │   └── EmployeeController.java        ← Exercises 4, 5, 6, 8
        │   ├── dto/
        │   │   ├── EmployeeSummary.java            ← Exercise 8 (interface projection)
        │   │   └── EmployeeDepartmentDTO.java       ← Exercise 8 (class projection)
        │   ├── model/
        │   │   ├── Department.java                ← Exercise 2
        │   │   └── Employee.java                   ← Exercises 2, 5, 10
        │   ├── repository/
        │   │   ├── DepartmentRepository.java       ← Exercise 3
        │   │   └── EmployeeRepository.java          ← Exercises 3, 5, 6, 8
        │   └── service/
        │       ├── BatchService.java                ← Exercise 10
        │       └── ReportingService.java            ← Exercise 9
        └── resources/
            └── application.properties               ← Exercises 1, 9, 10
```

---

## 3. Running the Project

### From IntelliJ
1. Open `EmployeeManagementSystemApplication.java`
2. Right-click → **Run 'EmployeeManagementSystemApplication.main()'**
3. On startup, `DataLoader` automatically:
   - Seeds 3 departments and 4 employees
   - Runs the two Named Queries from Exercise 5 and logs their results
   - Queries the secondary "reporting" datasource (Exercise 9)
   - Bulk-inserts 50 more employees via `BatchService` (Exercise 10),
     demonstrating JDBC batching in the Hibernate SQL logs
4. The app starts an embedded Tomcat server on **port 8080**.

### From command line
```bash
mvn spring-boot:run
```

### H2 Console
Browse to `http://localhost:8080/h2-console` and connect with:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

---

## 4. Exercise-by-Exercise Summary

### Exercise 1 — Setup
`pom.xml` declares Spring Data JPA, H2, Spring Web, and Lombok.
`application.properties` configures the H2 in-memory datasource exactly
as specified in the PDF.

### Exercise 2 — Entities
`Employee` (id, name, email, department) and `Department` (id, name)
with `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, and a
`@OneToMany`/`@ManyToOne` relationship between them.

### Exercise 3 — Repositories
`EmployeeRepository` and `DepartmentRepository` extend `JpaRepository`,
giving full CRUD with zero implementation code, plus a few derived query
methods as a starting example.

### Exercise 4 — CRUD via REST
`EmployeeController` and `DepartmentController` expose standard REST
endpoints:

```
GET    /api/departments
GET    /api/departments/{id}
POST   /api/departments
PUT    /api/departments/{id}
DELETE /api/departments/{id}

GET    /api/employees/{id}
POST   /api/employees
PUT    /api/employees/{id}
DELETE /api/employees/{id}
```

### Exercise 5 — Query Methods
Three approaches demonstrated side-by-side in `EmployeeRepository`:
- **Derived methods** — `findByNameContainingIgnoreCase`,
  `findByDepartmentId`, `findByEmailEndingWith`
- **`@Query` (JPQL + native)** — `searchByDepartmentId` /
  `searchByDepartmentIdNative`
- **Named Queries** — `@NamedQuery`/`@NamedQueries` defined directly on
  `Employee.java` (`Employee.findByDepartmentName`,
  `Employee.findByEmailDomain`), invoked both via the matching
  `findByDepartmentName` repository method (Spring Data resolves it by
  naming convention) and directly via
  `entityManager.createNamedQuery(...)` in `DataLoader`.

Exposed via: `GET /api/employees/search?name=` /
`?departmentId=` / `?emailDomain=`

### Exercise 6 — Pagination & Sorting
`EmployeeRepository.findAll(Pageable)` combined with a `PageRequest` +
`Sort` built from query parameters in `EmployeeController`:

```
GET /api/employees?page=0&size=10&sortBy=name&direction=asc
```

### Exercise 7 — Entity Auditing
`BaseAuditEntity` (a `@MappedSuperclass`) holds `@CreatedDate`,
`@LastModifiedDate`, `@CreatedBy`, `@LastModifiedBy`, wired up via
`@EntityListeners(AuditingEntityListener.class)`. Both `Employee` and
`Department` extend it. `@EnableJpaAuditing` is set on the main
application class, and `AuditorAwareImpl` supplies the "current user"
(a placeholder `"system"` value — swap in
`SecurityContextHolder` once Spring Security is added).

### Exercise 8 — Projections
- **Interface-based**: `EmployeeSummary` (closed projection on
  id/name/email, plus an "open" `@Value` SpEL-derived field combining
  name + department). Exposed via `GET /api/employees/summary`.
- **Class-based (DTO)**: `EmployeeDepartmentDTO`, populated via a JPQL
  constructor expression in `EmployeeRepository.findAllEmployeeDepartmentDTOs()`.
  Exposed via `GET /api/employees/with-department`.

### Exercise 9 — Data Source Configuration
`DataSourceConfig` declares two `DataSource` beans:
- `primaryDataSource` (`@Primary`) — bound to `spring.datasource.*`
- `reportingDataSource` — bound to the externalized `reporting.datasource.*`
  properties in `application.properties`

`ReportingService` shows how to inject the secondary datasource by
`@Qualifier` and use it via `JdbcTemplate`, independent of the primary
JPA-backed schema. (A note in the class Javadoc explains how to extend
this to a full second `EntityManagerFactory` if you need JPA entities
against the second database too.)

### Exercise 10 — Hibernate-Specific Features
- `@DynamicUpdate` / `@DynamicInsert` on `Employee` — only changed
  columns are included in generated UPDATE/INSERT statements.
- `@Version` on `Employee.version` — optimistic locking; a stale
  concurrent update throws `OptimisticLockException`.
- Batch processing configured in `application.properties`
  (`hibernate.jdbc.batch_size=30`, `order_inserts`, `order_updates`,
  `batch_versioned_data`) and exercised by `BatchService`, which
  `flush()`/`clear()`s the persistence context every 30 inserts to keep
  memory bounded during bulk loads. `DataLoader` calls
  `batchService.generateSampleEmployees(engineering, 50)` on startup so
  you can see the batched INSERTs in the console logs immediately.

---

## 5. Notes

- Lombok (`@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`,
  `@RequiredArgsConstructor`, `@ToString`) is used throughout to keep
  entity/controller code concise, exactly as requested in Exercise 1's
  dependency list.
- `spring.jpa.hibernate.ddl-auto=update` lets Hibernate create/update the
  H2 schema automatically from the entities — no manual DDL needed since
  this is an in-memory database that resets on every restart.
- Logging is set to `DEBUG` for `com.cognizant.ems` and `org.hibernate.SQL`
  (plus `TRACE` for bind parameters) so you can directly observe
  generated SQL, including the batched INSERT statements from Exercise 10.
- All REST endpoints can be tested directly from the H2 console for data
  inspection, or via `curl`/Postman against `http://localhost:8080`.
