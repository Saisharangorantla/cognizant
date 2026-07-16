# Exercise 2 - Inventory Management System with Service Discovery

Four Maven/Spring Boot projects:

- **eureka-server** (port 8761) - the service registry.
- **config-server** (port 8888) - centralized configuration, serving
  property files from `config-server/src/main/resources/config-repo`
  using Spring Cloud Config's **native** (filesystem) backend, so there's
  no external git repo to set up.
- **product-service** (port 8082) - manages products; registers with
  Eureka; pulls its `product-service.default-currency` and
  `greeting.message` properties from config-server.
- **inventory-service** (port 8083) - tracks stock per product;
  registers with Eureka; discovers `product-service` **by name** through
  Eureka (no hardcoded URL) via an OpenFeign client; pulls its
  `inventory.low-stock-threshold` from config-server.

## Startup order matters

Because `product-service` and `inventory-service` both declare
`spring.config.import: configserver:http://localhost:8888` (a hard
requirement, not `optional:`), they will fail to start if config-server
isn't up yet. Start things in this order:

1. `eureka-server`
2. `config-server`
3. `product-service`
4. `inventory-service`

## How to open in IntelliJ

Open all four folders as separate Maven projects (or as modules of one
parent IntelliJ project - there's no reactor `pom.xml` tying them
together, each is independently runnable).

## How to run / verify

1. Start all four apps in the order above.
2. Confirm both services registered: open `http://localhost:8761` in a
   browser - you should see `PRODUCT-SERVICE` and `INVENTORY-SERVICE`
   listed as registered instances.
3. Confirm centralized config was picked up:

   ```
   curl http://localhost:8082/config-check
   ```

   should print `Hello from product-service, configured centrally` -
   that string lives only in `config-repo/product-service.yml`, not in
   product-service's own `application.yml`.

4. Create a product:

   ```
   curl -X POST http://localhost:8082/api/products \
     -H "Content-Type: application/json" \
     -d '{"name":"Wireless Mouse","price":29.99}'
   ```

   Note the returned `id`.

5. Create an inventory record for it (replace `1` with that id):

   ```
   curl -X POST http://localhost:8083/api/inventory \
     -H "Content-Type: application/json" \
     -d '{"productId":1,"stockLevel":2}'
   ```

   `inventory-service` calls `product-service` first (resolved purely by
   the name `product-service` through Eureka) to confirm the product
   exists. Because `inventory-service`'s low-stock threshold from
   config-server is `3`, and we set stock to `2`, the response should
   come back with `"lowStock": true`.

## What to look at

- `config-server/src/main/resources/config-repo/*.yml` - the centrally
  managed properties.
- `product-service/.../web/ConfigCheckController.java` - proof config
  came from config-server.
- `inventory-service/.../client/ProductClient.java` - a Feign client
  with **no URL**, resolved entirely through Eureka.
- `eureka-server/src/main/resources/application.yml` - registry-only
  configuration (`register-with-eureka: false`, `fetch-registry: false`).
