package com.edu.domain.generator;

public class SequenceNumberUsedUpException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public SequenceNumberUsedUpException(String message) {
        super(message);
    }
}
