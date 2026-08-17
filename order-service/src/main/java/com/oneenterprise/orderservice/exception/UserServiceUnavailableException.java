package com.oneenterprise.orderservice.exception;

// Thrown when User Service cannot be reached, times out, or the circuit is open.
 
public class UserServiceUnavailableException extends RuntimeException {

    public UserServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserServiceUnavailableException(String message) {
        super(message);
    }
}
