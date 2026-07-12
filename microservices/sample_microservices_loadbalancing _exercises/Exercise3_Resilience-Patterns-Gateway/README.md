# Exercise 3 - Resilience Patterns in an API Gateway

Two Maven/Spring Boot projects:

- **unstable-service** (port 8082) - a downstream service with a single
  endpoint, `GET /unstable/hello`, that deliberately fails or hangs a
  good chunk of the time so we have something for the circuit breaker
  to react to.
- **resilient-gateway** (port 8080) - routes `/unstable/**` to
  `unstable-service`, wraps the route in a Resilience4j circuit breaker
  named `exampleCircuitBreaker`, and falls back to a local `/fallback`
  endpoint whenever a call fails, times out, or the breaker is open.

**One deliberate deviation from the exercise sheet's dependency list:**
Spring Cloud Gateway is reactive (built on WebFlux), so it needs
`spring-cloud-starter-circuitbreaker-reactor-resilience4j` rather than
the plain `resilience4j-spring-boot2` artifact - the latter is meant for
Spring MVC apps and doesn't expose the
`ReactiveResilience4JCircuitBreakerFactory` this project's configuration
class depends on. Everything else - the circuit breaker properties, the
`Customizer<ReactiveResilience4JCircuitBreakerFactory>` bean - matches
the exercise as given.

## How to open in IntelliJ

Open `resilient-gateway` and `unstable-service` as two separate Maven
projects (`File > Open...` on each `pom.xml`).

## How to run

1. Start `UnstableServiceApplication` (port 8082).
2. Start `ResilientGatewayApplication` (port 8080).
3. Hit the gateway repeatedly and watch the behaviour:

   ```
   for i in $(seq 1 20); do curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/unstable/hello; sleep 0.3; done
   ```

   You'll see a mix of `200`s and `503`s (the fallback) as calls fail or
   time out. Once enough calls fail within the configured sliding
   window (`slidingWindowSize=10`, `failureRateThreshold=50`), the
   breaker opens and every request gets an immediate `503` fallback
   response instead of waiting on `unstable-service` at all. After the
   configured `waitDurationInOpenState` (10s), it moves to half-open and
   starts testing the downstream service again.

4. Check the breaker's live state:

   ```
   curl http://localhost:8080/actuator/health
   curl http://localhost:8080/actuator/circuitbreakers
   ```

## What to look at

- `resilient-gateway/.../config/ResilienceConfiguration.java` - default
  circuit breaker / time limiter configuration.
- `resilient-gateway/.../web/FallbackController.java` - the fallback
  response.
- `resilient-gateway/src/main/resources/application.properties` - the
  route's `CircuitBreaker` filter and the `resilience4j.*` tuning
  properties.
- `unstable-service/.../web/UnstableController.java` - the simulated
  failures/timeouts.
