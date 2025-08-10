package com.wilton.matcha.common.file.service.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class LocalFileService {
    private final ResourceLoader resourceLoader;

    @Value("${matcha.file.storage.local-path}")
    private String basePath;

    @Autowired
    public LocalFileService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Resource read(String key) throws IOException {
        String location = Paths.get(basePath, key).toAbsolutePath().toString();
        Resource resource = resourceLoader.getResource(location);

        if (!resource.exists() || !resource.isReadable()) {
            throw new IOException("File not found or unreadable: " + location);
        }

        return resource;
    }

    public void save(String key, Resource resource) throws IOException {
        Path targetPath = Paths.get(basePath, key).toAbsolutePath();
        Files.createDirectories(targetPath.getParent());

        try (OutputStream out = new FileOutputStream(targetPath.toFile())) {
            StreamUtils.copy(resource.getInputStream(), out);
        }
    }

    public void delete(String key) throws IOException {
        Path fullPath = Paths.get(basePath, key).toAbsolutePath();
        Files.deleteIfExists(fullPath);
    }
}
