package com.oneenterprise.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body(ex.getMessage(), 404));
    }

    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<Map<String, Object>> handleUserServiceUnavailable(UserServiceUnavailableException ex) {
        // 502 Bad Gateway: our own API is fine, but the downstream dependency failed (Day 4).
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(body(ex.getMessage(), 502));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(body("An unexpected error occurred", 500));
    }

    private Map<String, Object> body(String message, int status) {
        return Map.of(
                "message", message,
                "status", status,
                "timestamp", Instant.now().toString()
        );
    }
}
