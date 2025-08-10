package com.wilton.matcha.common.file.repository;

import com.wilton.matcha.common.file.domain.ResourceMetadata;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResourceMetadataRepository extends MongoRepository<ResourceMetadata, String> {
    Optional<ResourceMetadata> findByKey(String key);

    void deleteByKey(String key);
}
