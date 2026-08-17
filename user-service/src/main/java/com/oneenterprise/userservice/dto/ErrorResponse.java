package com.oneenterprise.userservice.dto;

import java.time.Instant;

/**
 * Consistent, explicit error contract (Day 2: don't leak raw exceptions to clients).
 */
public record ErrorResponse(
        String message,
        int status,
        Instant timestamp
) {
    public static ErrorResponse of(String message, int status) {
        return new ErrorResponse(message, status, Instant.now());
    }
}
