# One Enterprise Platform — Microservices Reference Implementation (Days 1–6)

This project is a working implementation of the exercises from the Day 1–6 microservices
handbooks. It contains four independent Spring Boot applications:

| Module | Port | Role |
|---|---|---|
| `discovery-server` | 8761 | **(Day 6, new)** Eureka Service Registry. Dashboard at `http://localhost:8761`. |
| `user-service` | 8081 | Owns user data. Exposes `GET /users/{id}`. Registers with Eureka as `USER-SERVICE` (Day 6). |
| `order-service` | 8082 | Owns order data. Calls User Service via `RestClient`, wrapped with retry + circuit breaker. Exposes `GET /orders/{orderId}`. Registers with Eureka as `ORDER-SERVICE` (Day 6). |
| `gateway-service` | 8080 | API Gateway. Routes `/api/users/**` → User Service, `/api/orders/**` → Order Service, resolved via Eureka (`lb://USER-SERVICE`, `lb://ORDER-SERVICE`) instead of a hard-coded host/port (Day 6). |

## How each day maps to the code

- **Day 1** — Two independent Spring Boot apps (`user-service`, `order-service`), in-memory
  data, synchronous REST call from Order → User, running on different ports.
- **Day 2** — DTOs (`UserResponse`, `OrderResponse`) separate internal models from the public
  API contract. Errors are handled deliberately (`GlobalExceptionHandler`, `ErrorResponse`).
  The User Service URL is externalized to `application.properties`
  (`user.service.base-url`), not hard-coded in Java.
- **Day 3** — `gateway-service` sits in front of both backend services (`application.properties`
  routes). Data ownership boundaries are explicit: Order Service never touches User
  Service's data directly, only its API.
- **Day 4** — `order-service` applies resilience patterns around the User Service call:
  - **Timeout** — configured on the `RestClient` (`RestClientConfig`, 2s connect/read).
  - **Retry** — `@Retry(name = "userService")` on `UserClient.getUser`, max 3 attempts,
    300ms wait (see `application.properties`).
  - **Circuit breaker** — `@CircuitBreaker(name = "userService")`, opens after 50% failure
    rate over the last 10 calls, half-opens after 10s.
  - **Fallback** — `fallbackGetUser(...)` returns a meaningful
    `UserServiceUnavailableException` instead of a raw connection error; `OrderService`
    then returns a truthful "degraded" order response (HTTP 502) rather than pretending
    success.
- **Day 5** — `RestClientConfig` configures a reusable `RestClient` bean with an
  externalized base URL; `UserClient` encapsulates the HTTP call and DTO mapping so
  controllers never touch HTTP details directly.
- **Day 6** — Service discovery + gateway routing by logical name instead of fixed
  host/port:
  - **New module: `discovery-server`** — a Eureka Server (`@EnableEurekaServer`) on
    port 8761. Doesn't register with itself (`eureka.client.register-with-eureka=false`,
    `eureka.client.fetch-registry=false`). This is the registry every other service
    talks to.
  - **`user-service` / `order-service`** — each gets the
    `spring-cloud-starter-netflix-eureka-client` dependency and an
    `eureka.client.service-url.defaultZone=http://localhost:8761/eureka/` property.
    No Java code changes were needed: Spring Boot auto-registers the app under its
    `spring.application.name` (`USER-SERVICE` / `ORDER-SERVICE`) as soon as the
    dependency + property are present.
  - **`gateway-service`** — also becomes a Eureka client (same dependency +
    `defaultZone` property), and its two routes in `application.properties` were changed
    from hard-coded URIs (`http://localhost:8081`, `http://localhost:8082`) to
    `lb://USER-SERVICE` and `lb://ORDER-SERVICE`. The `lb://` scheme tells Spring
    Cloud Gateway to resolve the destination through Eureka rather than a fixed
    address, so routing keeps working even if an instance restarts on a different
    port or there are multiple instances.
  - **Not changed in this pass**: the direct `order-service → user-service` call
    (`RestClientConfig` / `UserClient`, Day 5) still uses the hard-coded
    `user.service.base-url` property rather than a load-balanced client. Only the
    Gateway's routing was switched to discovery, matching the Day 6 handbook's
    scope (Eureka Server + Gateway routing by service name).

## Running it

Each service is independently runnable. **Start the Discovery Server first** so the
other services have something to register with. Open four terminals from the project
root:

```bash
# Terminal 1 — start this first (Day 6)
cd discovery-server && mvn spring-boot:run

# Terminal 2
cd user-service && mvn spring-boot:run

# Terminal 3
cd order-service && mvn spring-boot:run

# Terminal 4 (gateway; Day 3 routing, Day 6 discovery-based)
cd gateway-service && mvn spring-boot:run
```

Once `discovery-server` is up, open `http://localhost:8761` to watch `USER-SERVICE`
and `ORDER-SERVICE` appear in the registry as they start.

Or build everything first from the root:

```bash
mvn clean install
```

### Try it

```bash
# Direct call to User Service
curl http://localhost:8081/users/1001

# Order Service calling User Service internally
curl http://localhost:8082/orders/5001

# Through the API Gateway (Day 3)
curl http://localhost:8080/api/users/1001
curl http://localhost:8080/api/orders/5001
```

### Day 4 hands-on: break it on purpose

```bash
# Stop user-service (Ctrl+C in its terminal), then:
curl http://localhost:8082/orders/5001
# -> HTTP 502 with a truthful "User Service is currently unavailable" message

# Watch the circuit breaker state:
curl http://localhost:8082/actuator/circuitbreakers
```

### Day 2 mini exercise: change the User Service location without touching Java

Edit `order-service/src/main/resources/application.properties`:

```properties
user.service.base-url=http://localhost:9091
```

Restart `order-service` only — no Java code changes needed.

### Day 6 hands-on: break it on purpose (discovery edition)

```bash
# With discovery-server, user-service, order-service, and gateway-service all running:
curl http://localhost:8080/api/users/1001
# -> works, gateway resolved USER-SERVICE via Eureka and routed the request

# Now stop user-service (Ctrl+C in its terminal), then immediately:
curl http://localhost:8080/api/users/1001
# -> fails once the registry's lease on USER-SERVICE expires / no instance is available

# Restart user-service, wait for it to re-register (check http://localhost:8761),
# then retry:
curl http://localhost:8080/api/users/1001
# -> works again, with no gateway config change and no restart of the gateway itself
```

This demonstrates the core Day 6 idea: the gateway never hard-codes where
`USER-SERVICE` lives — it asks the registry every time.

## Notes

- No real database is used (Day 1): all data is in-memory, intentionally, to keep the
  focus on service boundaries and communication rather than persistence.
- `gateway-service` uses Spring Cloud Gateway (reactive) — it's a separate module so
  `user-service`/`order-service` can stay on plain Spring MVC as in the handbooks.
- (Day 6) `discovery-server` must be the first service started, since `user-service`,
  `order-service`, and `gateway-service` all try to register with / query it on
  startup. They won't fail to start if it's briefly unavailable, but registration
  and `lb://` route resolution won't work until it's up.
