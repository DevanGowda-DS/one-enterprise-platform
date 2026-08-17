package com.oneenterprise.userservice.dto;

/**
 * Public API contract for a user (Day 2: DTOs separate the API from internal models).
 * Only fields that are intentionally exposed appear here.
 */
public record UserResponse(
        Long id,
        String name,
        String email
) {
}
