package com.edu.storage;

import com.edu.config.FileStorageProperties;
import com.edu.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageServiceImpl implements FileStorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageServiceImpl(FileStorageProperties properties) {
        this.rootLocation = Paths.get(properties.getRootDirectory());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Could not create root directory", e);
        }
    }

    @Override
    public String store(MultipartFile file) {

    }

    @Override
    public String store(String subFolder, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        if (student == null) {
            throw new FileStorageException("Could not store file while student information is missing");
        }
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) {
            throw new FileStorageException(
                    "Cannot store file with relative path outside current directory " + filename);
        }

        Path studentRootLocation = rootLocation.resolve(student.getId());
        try {
            Files.createDirectories(studentRootLocation);
        } catch (IOException e) {
            throw new FileStorageException("Could not create sub-directory for student " + student.getId());
        }

        UUID uuid = UUID.randomUUID();
        try {
            Files.copy(file.getInputStream(), studentRootLocation.resolve(uuid.toString()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + filename, e);
        }

        return uuid.toString();
    }

    private String doStore(String subFolder, MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Path subRootLocation;

        if (subFolder == null) {
            subRootLocation = rootLocation;
        } else {
            subRootLocation = rootLocation.resolve(subFolder);
            try {
                Files.createDirectories(subRootLocation);
            } catch (IOException e) {
                throw new FileStorageException("Could not create sub-directory " + subFolder);
            }
        }
        if (file.isEmpty()) {
            throw new FileStorageException("Failed to store empty file " + filename);
        }
        if (filename.contains("..")) { // Security concern
            throw new FileStorageException("Cannot store file with relative path outside current directory " + filename);
        }

        UUID uuid = UUID.randomUUID();
        String key;
        try {
            Files.copy(file.getInputStream(), subRootLocation.resolve(uuid.toString()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + filename, e);
        }

        return uuid.toString();
    }

    @Override
    public Resource load(String key) {
        // While loading, filename equals to key
        String filename = key;

        if (student == null) {
            throw new FileStorageException("Could not load file while student information is missing");
        }

        Path file = rootLocation.resolve(student.getId()).resolve(filename);

        try {
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }
}
