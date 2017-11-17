package com.edu.domain;

public class SequenceNumberUsedUpException extends RuntimeException {
    public SequenceNumberUsedUpException(String message) {
        super(message);
    }
}
