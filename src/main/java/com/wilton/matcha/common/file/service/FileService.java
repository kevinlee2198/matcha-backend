package com.wilton.matcha.common.file.service;

import com.wilton.matcha.common.file.service.dto.ResourceUploadRequest;
import com.wilton.matcha.common.file.service.dto.ResourceUploadResult;
import java.io.IOException;
import java.util.List;
import org.springframework.core.io.Resource;

public interface FileService {
    /**
     * Reads a resource from the underlying storage.
     *
     * @param key a path or identifier (e.g., filename or S3 key)
     * @return the resource.
     * @throws IOException if reading fails or resource not found
     */
    Resource read(String key) throws IOException;

    /**
     * Saves the given resource to the underlying storage with the provided key/path.
     *
     * @param request file to upload
     * @return the saved metadata of the resource
     */
    ResourceUploadResult save(ResourceUploadRequest request);

    /**
     * Saves the given resource to the underlying storage with the provided key/path.
     *
     * @param requests files to upload
     * @return the saved metadata of the resource
     */
    List<ResourceUploadResult> save(List<ResourceUploadRequest> requests);

    /**
     * Deletes the given resource
     * @param key a path or identifier (e.g., filename or S3 key)
     * @throws IOException if file cannot be deleted
     */
    void delete(String key) throws IOException;
}
