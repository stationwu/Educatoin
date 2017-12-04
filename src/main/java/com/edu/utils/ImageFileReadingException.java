package com.edu.utils;

public class ImageFileReadingException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ImageFileReadingException(String message) {
        super(message);
    }

    public ImageFileReadingException(String message, Throwable e) {
        super(message, e);
    }
}
