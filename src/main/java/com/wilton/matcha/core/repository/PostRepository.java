package com.wilton.matcha.core.repository;

import com.wilton.matcha.core.domain.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {}
