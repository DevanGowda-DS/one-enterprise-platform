# One Enterprise Platform — Microservices Reference Implementation (Days 1–5)

This project is a working implementation of the exercises from the Day 1–5 microservices
handbooks. It contains three independent Spring Boot applications:

| Module | Port | Role |
|---|---|---|
| `user-service` | 8081 | Owns user data. Exposes `GET /users/{id}`. |
| `order-service` | 8082 | Owns order data. Calls User Service via `RestClient`, wrapped with retry + circuit breaker. Exposes `GET /orders/{orderId}`. |
| `gateway-service` | 8080 | API Gateway. Routes `/api/users/**` → User Service, `/api/orders/**` → Order Service. |

## How each day maps to the code

- **Day 1** — Two independent Spring Boot apps (`user-service`, `order-service`), in-memory
  data, synchronous REST call from Order → User, running on different ports.
- **Day 2** — DTOs (`UserResponse`, `OrderResponse`) separate internal models from the public
  API contract. Errors are handled deliberately (`GlobalExceptionHandler`, `ErrorResponse`).
  The User Service URL is externalized to `application.properties`
  (`user.service.base-url`), not hard-coded in Java.
- **Day 3** — `gateway-service` sits in front of both backend services (`application.yml`
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

## Running it

Each service is independently runnable. Open three terminals from the project root:

```bash
# Terminal 1
cd user-service && mvn spring-boot:run

# Terminal 2
cd order-service && mvn spring-boot:run

# Terminal 3 (optional, for the Day 3 gateway exercise)
cd gateway-service && mvn spring-boot:run
```

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

## Notes

- No real database is used (Day 1): all data is in-memory, intentionally, to keep the
  focus on service boundaries and communication rather than persistence.
- `gateway-service` uses Spring Cloud Gateway (reactive) — it's a separate module so
  `user-service`/`order-service` can stay on plain Spring MVC as in the handbooks.
