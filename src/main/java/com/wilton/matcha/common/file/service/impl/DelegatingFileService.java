package com.wilton.matcha.common.file.service.impl;

import com.wilton.matcha.common.exception.ResourceNotFoundException;
import com.wilton.matcha.common.file.StorageType;
import com.wilton.matcha.common.file.domain.ResourceMetadata;
import com.wilton.matcha.common.file.repository.ResourceMetadataRepository;
import com.wilton.matcha.common.file.service.FileService;
import com.wilton.matcha.common.file.service.dto.ResourceUploadRequest;
import com.wilton.matcha.common.file.service.dto.ResourceUploadResult;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class DelegatingFileService implements FileService {
    private final LocalFileService localFileService;
    private final RemoteFileService remoteFileService;
    private final ResourceMetadataRepository resourceMetadataRepository;

    @Autowired
    public DelegatingFileService(
            LocalFileService localFileService,
            RemoteFileService remoteFileService,
            ResourceMetadataRepository resourceMetadataRepository) {
        this.localFileService = localFileService;
        this.remoteFileService = remoteFileService;
        this.resourceMetadataRepository = resourceMetadataRepository;
    }

    @Override
    public Resource read(String key) throws IOException {
        Optional<ResourceMetadata> resourceMetadataOptional = resourceMetadataRepository.findByKey(key);
        if (resourceMetadataOptional.isEmpty()) {
            throw new ResourceNotFoundException("Resource", "key", key);
        }
        ResourceMetadata resourceMetadata = resourceMetadataOptional.get();

        Resource resource;
        switch (resourceMetadata.getStorageType()) {
            case LOCAL -> resource = localFileService.read(key);
            case REMOTE -> resource = remoteFileService.read(key);
            default -> throw new IOException("Unsupported file storage type");
        }

        return resource;
    }

    @Override
    public ResourceUploadResult save(ResourceUploadRequest request) {
        ResourceUploadResult result;
        String key = request.key();
        Resource resource = request.resource();
        StorageType storageType = request.storageType();
        try {
            switch (storageType) {
                case LOCAL -> localFileService.save(key, resource);
                case REMOTE -> remoteFileService.save(key, resource);
                default -> throw new IllegalArgumentException("StorageType " + storageType + " is illegal");
            }
            ResourceMetadata resourceMetadata = new ResourceMetadata();
            resourceMetadata.setKey(key);
            resourceMetadata.setFilename(resource.getFilename());
            resourceMetadata.setSize(resource.contentLength());
            resourceMetadata.setStorageType(storageType);
            resourceMetadata = resourceMetadataRepository.save(resourceMetadata);
            result = new ResourceUploadResult(key, true, null, resourceMetadata);
        } catch (Exception e) {
            result = new ResourceUploadResult(key, false, e, null);
        }

        return result;
    }

    @Override
    public List<ResourceUploadResult> save(List<ResourceUploadRequest> requests) {
        return requests.stream().map(this::save).toList();
    }

    @Override
    public void delete(String key) throws IOException {
        Optional<ResourceMetadata> resourceMetadataOptional = resourceMetadataRepository.findByKey(key);

        if (resourceMetadataOptional.isEmpty()) {
            return;
        }

        ResourceMetadata resourceMetadata = resourceMetadataOptional.get();
        switch (resourceMetadata.getStorageType()) {
            case LOCAL -> localFileService.delete(key);
            case REMOTE -> remoteFileService.delete(key);
        }

        resourceMetadataRepository.deleteById(resourceMetadata.getId());
    }
}
