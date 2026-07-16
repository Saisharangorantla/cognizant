# Exercise 3 - API Gateway (Routing, Rate Limiting, Caching, Path Rewriting)

Three Maven/Spring Boot projects:

- **customer-service** (port 8081) - exposes `/customers` and `/customers/{id}`.
- **billing-service** (port 8082) - exposes `/billing` and `/billing/{invoiceId}`.
- **api-gateway** (port 8080) - fronts both services under
  `/api/customers/**` and `/api/billing/**`, and applies:
  - **Path rewriting** - `RewritePath` strips the `/api` prefix and
    forwards to the backend's actual path (`/customers/**`, `/billing/**`).
  - **Rate limiting** - a custom `RateLimit` gateway filter (see below).
  - **Caching** - Spring Cloud Gateway's built-in `LocalResponseCache`
    filter (Caffeine-backed, in-memory).

## About the rate limiter

Spring Cloud Gateway's built-in `RequestRateLimiter` filter needs a
Redis-backed implementation out of the box, which would mean standing up
Redis just to run this exercise. Instead, `RateLimitGatewayFilterFactory`
implements a small in-memory token-bucket limiter with
[Bucket4j](https://bucket4j.com/), keyed by client IP, and registered as
a proper Gateway filter factory (usable in `application.yml` exactly like
a built-in one, under the name `RateLimit`). Each route below is
configured for 5 requests per 10-second window per client IP.

(For a real multi-instance gateway deployment behind a load balancer,
you'd want a shared store like Redis instead of each gateway instance
tracking its own in-memory buckets - noted here rather than added, to
keep this runnable without extra infrastructure.)

## How to open in IntelliJ

Open all three folders as separate Maven projects.

## How to run

1. Start `CustomerServiceApplication` (port 8081).
2. Start `BillingServiceApplication` (port 8082).
3. Start `ApiGatewayApplication` (port 8080).
4. Call through the gateway:

   ```
   curl -i http://localhost:8080/api/customers
   curl -i http://localhost:8080/api/billing
   ```

   Both should succeed even though customer-service/billing-service only
   know about `/customers` and `/billing` respectively - the gateway
   rewrote the path.

5. **See the rate limiter kick in** - fire more than 5 requests quickly:

   ```
   for i in $(seq 1 8); do curl -s -o /dev/null -w "%{http_code}\n" http://localhost:8080/api/customers; done
   ```

   The first 5 should return `200`, the rest `429 Too Many Requests`
   (with a `Retry-After` header). Wait 10 seconds and the bucket refills.

6. **See the cache in action** - the customer-service and billing-service
   controllers stamp each response with a `servedAt` timestamp. Call the
   same URL twice in a row through the gateway within 30 seconds and
   compare `servedAt`:

   ```
   curl -s http://localhost:8080/api/customers/1
   curl -s http://localhost:8080/api/customers/1
   ```

   The second call's `servedAt` should be identical to the first - it
   was served from the gateway's local cache rather than hitting
   customer-service again. After the 30s TTL expires, a new call fetches
   fresh data.

## What to look at

- `api-gateway/.../filter/RateLimitGatewayFilterFactory.java` - the
  custom Bucket4j-backed rate limiter.
- `api-gateway/src/main/resources/application.yml` - route definitions:
  `RewritePath`, `RateLimit`, and `LocalResponseCache` filters.
