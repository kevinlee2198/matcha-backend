package com.wilton.matcha.common.file.service.impl;

import com.wilton.matcha.common.file.domain.ResourceMetadata;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class RemoteFileService {
    public Resource read(String key) throws IOException {
        return null;
    }

    public ResourceMetadata save(String key, Resource resource) throws IOException {
        return null;
    }

    public void delete(String key) throws IOException {}
}
