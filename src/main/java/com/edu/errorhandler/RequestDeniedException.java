package com.edu.errorhandler;

public class RequestDeniedException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public RequestDeniedException(String message) {
        super(message);
    }

    public RequestDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
