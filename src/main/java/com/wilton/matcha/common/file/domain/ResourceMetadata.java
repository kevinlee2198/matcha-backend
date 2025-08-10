package com.wilton.matcha.common.file.domain;

import com.wilton.matcha.common.domain.AbstractAuditingEntity;
import com.wilton.matcha.common.file.StorageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ResourceMetadata extends AbstractAuditingEntity<String> {
    @Id
    private String id;

    private String key; // Path or storage key
    private String filename;
    private long size;
    private StorageType storageType;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public StorageType getStorageType() {
        return storageType;
    }

    public void setStorageType(StorageType storageType) {
        this.storageType = storageType;
    }
}
