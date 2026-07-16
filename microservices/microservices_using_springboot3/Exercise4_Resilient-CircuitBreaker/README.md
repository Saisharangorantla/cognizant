# Exercise 4 - Resilient Microservices with Circuit Breaker

Two Maven/Spring Boot projects:

- **slow-third-party-service** (port 8091) - stands in for the "slow
  third-party API" from the exercise. `POST /external/authorize`
  randomly rejects (~30%), hangs for 3.5s (~20%, long enough to blow
  past payment-service's timeout), or responds normally with some
  natural latency.
- **payment-service** (port 8080) - calls it through a Resilience4j
  circuit breaker + time limiter, with a fallback and full event
  logging/monitoring.

## How the resilience is wired together

- `ThirdPartyAuthorizationClient` - the plain HTTP call (via `RestClient`,
  with a 2s connect / 5s read timeout as a backstop).
- `ThirdPartyPaymentGateway.charge(...)` - annotated with
  `@CircuitBreaker(name = "paymentService", fallbackMethod = "fallback")`
  and `@TimeLimiter(name = "paymentService")`. `@TimeLimiter` needs an
  async return type to actually be able to time out a call, so the
  blocking HTTP call is pushed onto `CompletableFuture.supplyAsync(...)`.
- `fallback(...)` - returns a `PENDING` result instead of propagating the
  failure, and logs why the fallback was triggered.
- `CircuitBreakerEventLogger` - subscribes to the circuit breaker's event
  stream and logs every state transition (`CLOSED -> OPEN`, etc.),
  rejected call, and recorded error - this is the "log and monitor
  fallback events" part of the exercise.
- `resilience4j-spring-boot3` (not `-spring-boot2`) is the correct
  starter for a synchronous Spring MVC app on Boot 3; it's what wires up
  `@CircuitBreaker`/`@TimeLimiter` via AOP and exposes the actuator
  endpoints below.

## How to open in IntelliJ

Open `slow-third-party-service` and `payment-service` as two separate
Maven projects.

## How to run

1. Start `SlowThirdPartyServiceApplication` (port 8091).
2. Start `PaymentServiceApplication` (port 8080).
3. Fire off a batch of charge requests and watch the mix of outcomes:

   ```
   for i in $(seq 1 20); do
     curl -s -X POST http://localhost:8080/payments/charge \
       -H "Content-Type: application/json" \
       -d "{\"orderId\":\"order-$i\",\"amount\":25.00}"
     echo
     sleep 0.3
   done
   ```

   You'll see a mix of `{"status":"APPROVED",...}` and
   `{"status":"PENDING",...}` (the fallback) responses. Watch
   payment-service's console output - `CircuitBreakerEventLogger` logs
   every failure and, once enough calls fail within the sliding window
   (`sliding-window-size: 10`, `failure-rate-threshold: 50`), a
   `CLOSED -> OPEN` transition. While open, calls get an immediate
   fallback without even hitting the third-party service; after
   `wait-duration-in-open-state` (10s) it moves to half-open and starts
   testing again.

4. Check the breaker's live state and recent events:

   ```
   curl http://localhost:8080/actuator/health
   curl http://localhost:8080/actuator/circuitbreakers
   curl http://localhost:8080/actuator/circuitbreakerevents
   ```

## What to look at

- `service/ThirdPartyPaymentGateway.java` - the circuit breaker/time
  limiter/fallback wiring.
- `config/CircuitBreakerEventLogger.java` - event logging/monitoring.
- `src/main/resources/application.yml` - the `resilience4j.*` tuning.
