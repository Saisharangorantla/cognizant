# Exercise 2 - Load Balancing in an API Gateway

Two Maven/Spring Boot projects:

- **example-service** - a downstream service you run **twice**, on two
  different ports, to simulate two instances of the same microservice.
- **api-gateway** (port 8080) - routes `/loadbalanced/**` to
  `lb://example-service` and chooses between the two instances using a
  custom `RandomLoadBalancer` (instead of the default round-robin
  strategy).

There's no Eureka/Consul here on purpose, to keep the exercise
self-contained. The two `example-service` instances are declared to the
gateway with Spring Cloud's built-in `SimpleDiscoveryClient`, configured in
`api-gateway/src/main/resources/application.properties`.

## How to open in IntelliJ

Open `api-gateway` and `example-service` as two separate Maven projects
(`File > Open...` on each `pom.xml`).

## How to run two instances of example-service

You need `example-service` running twice, on ports 8081 and 8082, using
the `instance1` / `instance2` Spring profiles.

**From the command line** (run each in its own terminal):

```
cd example-service
mvn spring-boot:run -Dspring-boot.run.profiles=instance1
```

```
cd example-service
mvn spring-boot:run -Dspring-boot.run.profiles=instance2
```

**From IntelliJ:** duplicate the generated `ExampleServiceApplication` run
configuration (right-click it -> Copy Configuration), and on each copy set
"Active profiles" to `instance1` and `instance2` respectively. Run both.

## Running the gateway and testing load balancing

Start `ApiGatewayApplication` (port 8080), then call the gateway
repeatedly:

```
curl http://localhost:8080/loadbalanced/hello
curl http://localhost:8080/loadbalanced/hello
curl http://localhost:8080/loadbalanced/hello
curl http://localhost:8080/loadbalanced/hello
```

Look at the `handledByInstanceOnPort` field in the response - because the
gateway is using `RandomLoadBalancer`, you should see it flip between
`8081` and `8082` across repeated calls (not a strict alternating pattern,
since it's random).

## What to look at

- `api-gateway/.../loadbalancer/CustomLoadBalancerConfiguration.java` -
  the custom `ReactorLoadBalancer` bean.
- `api-gateway/.../ApiGatewayApplication.java` - the `@LoadBalancerClient`
  annotation that ties the custom configuration to the `example-service`
  client name.
- `api-gateway/src/main/resources/application.properties` - the static
  instance list and the `lb://` route.
