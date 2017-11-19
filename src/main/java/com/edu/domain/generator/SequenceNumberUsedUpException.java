package com.edu.domain.generator;

public class SequenceNumberUsedUpException extends RuntimeException {
    public SequenceNumberUsedUpException(String message) {
        super(message);
    }
}
