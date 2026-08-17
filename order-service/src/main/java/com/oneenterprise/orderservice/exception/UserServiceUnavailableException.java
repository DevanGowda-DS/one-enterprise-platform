package com.oneenterprise.orderservice.exception;

/**
 * Thrown when User Service cannot be reached, times out, or the circuit is open.
 * Day 4 lesson: this is a deliberate, meaningful error rather than a leaked
 * low-level connection exception.
 */
public class UserServiceUnavailableException extends RuntimeException {

    public UserServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserServiceUnavailableException(String message) {
        super(message);
    }
}
