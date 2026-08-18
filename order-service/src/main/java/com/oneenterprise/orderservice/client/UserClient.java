package com.oneenterprise.orderservice.client;

import com.oneenterprise.orderservice.dto.UserResponse;
import com.oneenterprise.orderservice.exception.UserServiceUnavailableException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 *
 * Day 4 resilience patterns applied here:
 *  - Timeout: configured on the underlying RestClient (see RestClientConfig).
 *  - Retry: a small, limited number of attempts for transient failures.
 *  - Circuit breaker: stops hammering User Service once it's clearly unhealthy.
 *  - Fallback: returns a meaningful exception instead of a raw connection error.
 */
@Service
public class UserClient {

    private static final Logger log = LoggerFactory.getLogger(UserClient.class);

    private final RestClient restClient;

    public UserClient(RestClient userRestClient) {
        this.restClient = userRestClient;
    }

    @Retry(name = "userService", fallbackMethod = "fallbackGetUser")
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUser")
    public UserResponse getUser(Long id) {
        log.info("Calling User Service for user id={}", id);
        try {
            return restClient.get()
                    .uri("/users/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
                        throw new UserServiceUnavailableException(
                                "User Service reported user not found or invalid request: " + id);
                    })
                    .body(UserResponse.class);
        } catch (RestClientResponseException ex) {
            throw new UserServiceUnavailableException("User Service returned an error", ex);
        }
    }

    /**
     * Fallback used when retries are exhausted or the circuit breaker is open.
     * Signature must match getUser's params + a Throwable (Day 4: truthful fallback,
     * not pretending success).
     */
    private UserResponse fallbackGetUser(Long id, Throwable throwable) {
        log.warn("Falling back for user id={} due to: {}", id, throwable.toString());
        throw new UserServiceUnavailableException(
                "User Service is currently unavailable. Please try again later.", throwable);
    }
}
