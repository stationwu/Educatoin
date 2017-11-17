package com.edu.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    /**
     * Do whatever initialization work at startup
     */
    void init();

    /**
     * Stores one file and returns a key
     * @param subFolder
     * @param file
     * @return key
     */
    String store(String subFolder, MultipartFile file);

    /**
     * Stores one file and returns a key
     * @param file
     * @return key
     */
    String store(MultipartFile file);

    /**
     * Load the file using the key
     * @param key
     * @return resource
     */
    Resource load(String key);

    /**
     * Load the small version image using the key
     * @param key
     * @return resource
     */
    Resource loadSmallVersion(String key);

    /**
     * Load the thumbnail using the key
     * @param key
     * @return resource
     */
    Resource loadThumbnail(String key);
}
