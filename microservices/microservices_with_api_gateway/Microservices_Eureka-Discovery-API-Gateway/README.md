# Microservices with Eureka Discovery and API Gateway (Bank Exercise)

Five small Maven/Spring Boot projects, matching the walkthrough in the
exercise sheet end-to-end:

| Project          | Port | Role                                                              |
|------------------|------|--------------------------------------------------------------------|
| `eureka-server`  | 8761 | Service registry                                                  |
| `account-service`| 8080 | `GET /accounts/{number}` - dummy account lookup                   |
| `loan-service`   | 8081 | `GET /loans/{number}` - dummy loan lookup                         |
| `greet-service`  | 8082 | `GET /greet` -> "Hello World!!"                                   |
| `api-gateway`    | 9090 | Routes to any registered service by name; logs every request      |

## What changed vs. the exercise sheet, and why

- The sheet was written against **Spring Boot 2.5.5 / Spring Cloud
  2020.0.4** (visible in the screenshots). These projects use current
  **Spring Boot 3.2.5 / Spring Cloud 2023.0.1** instead, so they build
  and run against what `start.spring.io` actually offers today. The
  mechanics are otherwise identical - same annotations
  (`@EnableEurekaServer`, `@EnableDiscoveryClient`), same properties
  (`eureka.client.register-with-eureka`,
  `spring.cloud.gateway.discovery.locator.enabled`, etc.), same
  `GlobalFilter` pattern for `LogFilter`.
- `@EnableDiscoveryClient` is included on `account-service` and
  `loan-service` to match the sheet, even though it's effectively a
  no-op in current Spring Cloud - just having the Eureka client starter
  on the classpath is enough to auto-register.
- Ports were spread out (8080/8081/8082/8761/9090) so all five services
  can run **at the same time** on one machine, instead of the sheet's
  approach of stopping/starting services one at a time to work around
  port clashes.
- `greet-service` also registers with Eureka (the sheet has it start
  standalone on 8080 first, then separately shows it registering) - here
  it's wired up from the start alongside account/loan, since all three
  are meant to end up reachable through the same gateway.

## How to open in IntelliJ

Open all five folders as separate Maven projects.

## How to run (start in this order)

1. `EurekaServerApplication` - wait until it's fully up, then check
   `http://localhost:8761` in a browser. "Instances currently registered
   with Eureka" should be empty.
2. `AccountServiceApplication`, `LoanServiceApplication`,
   `GreetServiceApplication` - any order, each takes a few seconds to
   register. Refresh `http://localhost:8761` and you should see
   `ACCOUNT-SERVICE`, `LOAN-SERVICE`, and `GREET-SERVICE` listed.
3. `ApiGatewayApplication` - once up, it also shows in the Eureka
   registry as `API-GATEWAY`.

## Try it

Call each service directly first:

```
curl http://localhost:8080/accounts/00987987973432
curl http://localhost:8081/loans/H00987987972342
curl http://localhost:8082/greet
```

Then call the same services **through the gateway**, using the
lower-cased service name as the path prefix (no route configuration
needed anywhere - `discovery.locator.enabled=true` derives the routes
straight from the Eureka registry):

```
curl http://localhost:9090/account-service/accounts/00987987973432
curl http://localhost:9090/loan-service/loans/H00987987972342
curl http://localhost:9090/greet-service/greet
```

Watch `api-gateway`'s console while you do this - `LogFilter` logs a
line like:

```
====>Request URL http://localhost:9090/greet-service/greet
```

for every request that passes through, regardless of which downstream
service it's headed for.

## What to look at

- `eureka-server/src/main/resources/application.properties` -
  registry-only configuration.
- `api-gateway/.../filter/LogFilter.java` - the global logging filter.
- `api-gateway/src/main/resources/application.properties` - the
  discovery-locator routing properties that make explicit route
  definitions unnecessary here.
