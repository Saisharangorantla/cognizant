# Exercise 1 - Edge Service: Routing and Filtering

This exercise contains two independent Maven/Spring Boot projects:

- **backend-service** (port 8081) - a small downstream service with a single
  endpoint, `GET /example/hello`, that stands in for "the rest of the
  microservices architecture."
- **edge-service** (port 8080) - the Spring Cloud Gateway edge service. It
  routes any request under `/example/**` to `backend-service`, adds a
  response header via a route filter, and logs every request/response
  through a custom `GlobalFilter` (`LoggingFilter`).

## How to open in IntelliJ

1. Open IntelliJ IDEA -> `File > Open...` and pick the `backend-service`
   folder (its `pom.xml`). Let IntelliJ import the Maven project.
2. Repeat for the `edge-service` folder, either as a second window or by
   adding it as a module to an existing project.
3. Each folder is a self-contained Maven project (`pom.xml` at the root),
   so IntelliJ will recognize them as runnable Spring Boot applications
   without any extra configuration.

## How to run

1. Start `BackendServiceApplication` first (right-click it in IntelliJ ->
   Run, or `mvn spring-boot:run` from the `backend-service` folder). It
   starts on port `8081`.
2. Start `EdgeServiceApplication` (or `mvn spring-boot:run` from the
   `edge-service` folder). It starts on port `8080`.
3. Call the gateway directly:

   ```
   curl -i http://localhost:8080/example/hello
   ```

   You should see the JSON response from `backend-service`, an
   `X-Response-Origin: edge-service` header added by the gateway's route
   filter, and a log line in the edge-service console from
   `LoggingFilter` showing the method, path, status code and latency.

## What to look at

- `edge-service/src/main/resources/application.properties` - the route
  definition (`Path=/example/**` -> `http://localhost:8081`).
- `edge-service/.../filter/LoggingFilter.java` - the custom global filter
  used for request logging.
