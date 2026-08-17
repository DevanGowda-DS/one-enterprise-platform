package com.oneenterprise.userservice.dto;


public record UserResponse(
        Long id,
        String name,
        String email
) {
}
