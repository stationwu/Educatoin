package com.edu.utils;

public class ImageFileReadingException extends RuntimeException {
    public ImageFileReadingException(String message) {
        super(message);
    }

    public ImageFileReadingException(String message, Throwable e) {
        super(message, e);
    }
}
