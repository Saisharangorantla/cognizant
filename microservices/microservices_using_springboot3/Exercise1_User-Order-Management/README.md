# Exercise 1 - User and Order Management System

Two Maven/Spring Boot projects that talk to each other over HTTP:

- **user-service** (port 8081) - CRUD for users, backed by JPA.
- **order-service** (port 8082) - CRUD for orders, backed by JPA, and
  calls `user-service` via **OpenFeign** to validate the user before
  placing an order (and to enrich order responses with the customer's
  name).

## Database

Both services run against an **in-memory H2 database by default** - no
setup required, and each restart starts with a clean slate. That
satisfies "runs out of the box"; to satisfy "store data in
MySQL/PostgreSQL", each project also ships an `application-postgres.yml`
profile (PostgreSQL driver is already on the classpath). To use it:

```
docker run -p 5432:5432 -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=userdb postgres:16
```

then run with `--spring.profiles.active=postgres` (create a matching
`orderdb` database, or point both at the same one). Swap in a MySQL
driver/dialect the same way if you'd rather use MySQL.

Each service's H2 console is available at `/h2-console` while running
with the default profile (JDBC URL shown in `application.yml`).

## How to open in IntelliJ

Open `user-service` and `order-service` as two separate Maven projects.

## How to run

1. Start `UserServiceApplication` (port 8081).
2. Start `OrderServiceApplication` (port 8082).
3. Create a user:

   ```
   curl -X POST http://localhost:8081/api/users \
     -H "Content-Type: application/json" \
     -d '{"fullName":"Ada Lovelace","email":"ada@example.com"}'
   ```

   Note the returned `id`.

4. Place an order for that user (replace `1` with the id from step 3):

   ```
   curl -X POST http://localhost:8082/api/orders \
     -H "Content-Type: application/json" \
     -d '{"userId":1,"productName":"Mechanical Keyboard","quantity":1,"totalAmount":129.99}'
   ```

   `order-service` calls `user-service` first to confirm the user
   exists; if you pass a non-existent `userId` you'll get a `404`
   instead of an order being created.

5. List orders (each one is enriched with the customer's name, fetched
   live from `user-service`):

   ```
   curl http://localhost:8082/api/orders
   ```

## What to look at

- `order-service/.../client/UserClient.java` - the Feign client
  interface.
- `order-service/.../config/FeignErrorConfig.java` - translating a 404
  from user-service into a clean error here.
- `order-service/.../web/OrderController.java` - where the cross-service
  call happens, including graceful degradation in `findAll`/`findById`
  if user-service is temporarily unreachable.
