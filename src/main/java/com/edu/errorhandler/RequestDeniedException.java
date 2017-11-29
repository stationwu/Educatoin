package com.edu.errorhandler;

public class RequestDeniedException extends RuntimeException {
    public RequestDeniedException(String message) {
        super(message);
    }

    public RequestDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
