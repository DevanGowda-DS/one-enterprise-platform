package com.oneenterprise.orderservice.exception;


public class UserServiceUnavailableException extends RuntimeException {

    public UserServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserServiceUnavailableException(String message) {
        super(message);
    }
}
