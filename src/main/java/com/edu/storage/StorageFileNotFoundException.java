package com.edu.storage;

public class StorageFileNotFoundException extends FileStorageException {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
