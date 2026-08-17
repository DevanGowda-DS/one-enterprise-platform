package com.oneenterprise.userservice.controller;

import com.oneenterprise.userservice.dto.UserResponse;
import com.oneenterprise.userservice.service.UserService;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Public API contract for User Service (Day 1 + Day 2).
 * GET /users/{id} -> 200 with UserResponse, or 404 (handled by GlobalExceptionHandler).
 */
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable @Positive Long id) {
        return userService.getUser(id);
    }
}
