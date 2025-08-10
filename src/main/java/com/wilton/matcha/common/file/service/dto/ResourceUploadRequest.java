package com.wilton.matcha.common.file.service.dto;

import com.wilton.matcha.common.file.StorageType;
import org.springframework.core.io.Resource;

public record ResourceUploadRequest(String key, Resource resource, StorageType storageType) {}
