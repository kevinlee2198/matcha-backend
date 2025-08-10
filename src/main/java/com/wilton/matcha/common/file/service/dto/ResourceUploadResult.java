package com.wilton.matcha.common.file.service.dto;

import com.wilton.matcha.common.file.domain.ResourceMetadata;

public record ResourceUploadResult(String key, boolean success, Exception error, ResourceMetadata resourceMetadata) {}
