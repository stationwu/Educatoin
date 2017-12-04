package com.edu.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

public interface FileStorageService {
    /**
     * Do whatever initialization work at startup
     */
    void init();

    /**
     * Stores one file and returns a key for reload
     * @param subFolder
     * @param file
     * @return key
     */
    String store(String subFolder, MultipartFile file);

    /**
     * Stores one file and returns a key for reload
     * @param file
     * @return key
     */
    String store(MultipartFile file);

    /**
     * Stores an image file and returns a key for reload
     * @param image
     * @return
     */
    String store(String subFolder, BufferedImage image);

    /**
     * Stores an image file and returns a key for reload
     * @param image
     * @return
     */
    String store(BufferedImage image);

    /**
     * Load the file using the key
     * @param key
     * @return resource
     */
    Resource load(String key);

    /**
     * Never call it unless you're sure about you won't pay for it
     */
    void deleteAll();
}
