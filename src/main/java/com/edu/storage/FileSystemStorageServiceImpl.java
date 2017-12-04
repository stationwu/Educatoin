package com.edu.storage;

import com.edu.config.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
        return doStore(null, file);
    }

    @Override
    public String store(String subFolder, MultipartFile file) {
        if (subFolder == null) {
            throw new FileStorageException("Invalid sub folder name " + subFolder);
        }
        return doStore(subFolder, file);
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
        Path destinationPath = subRootLocation.resolve(uuid.toString());
        try {
            Files.copy(file.getInputStream(), destinationPath,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file " + filename, e);
        }

        return destinationPath.toString();
    }

    @Override
    public String store(String subFolder, BufferedImage image) {
        if (subFolder == null) {
            throw new FileStorageException("Invalid sub folder name " + subFolder);
        }
        return doStore(subFolder, image);
    }

    @Override
    public String store(BufferedImage image) {
        return doStore(null, image);
    }

    private String doStore(String subFolder, BufferedImage image) {
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

        UUID uuid = UUID.randomUUID();
        Path destinationPath = subRootLocation.resolve(uuid.toString());
        try {
            File file = new File(destinationPath.toString());
            ImageIO.write(image, "jpg", file);
        } catch (IOException e) {
            throw new FileStorageException("Failed to store image file", e);
        }

        return destinationPath.toString();
    }

    @Override
    public Resource load(String path) {
        Path fileLocation = Paths.get(path);

        try {
            Resource resource = new UrlResource(fileLocation.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + fileLocation);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + fileLocation, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }
}
