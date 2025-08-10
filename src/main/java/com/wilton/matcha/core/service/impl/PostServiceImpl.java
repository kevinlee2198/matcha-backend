package com.wilton.matcha.core.service.impl;

import com.wilton.matcha.common.domain.MatchaUser;
import com.wilton.matcha.core.domain.Post;
import com.wilton.matcha.core.repository.PostRepository;
import com.wilton.matcha.core.service.PostService;
import com.wilton.matcha.core.service.dto.PostSaveRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ConversionService conversionService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, ConversionService conversionService) {
        this.postRepository = postRepository;
        this.conversionService = conversionService;
    }

    @Override
    public Optional<Post> getPost(String id, MatchaUser matchaUser) {
        return postRepository.findById(id);
    }

    @Override
    public Post savePost(PostSaveRequest input, MatchaUser matchaUser) {
        Post post = conversionService.convert(input, Post.class);
        if (post == null) {
            throw new ConversionFailedException(
                    TypeDescriptor.valueOf(PostSaveRequest.class),
                    TypeDescriptor.valueOf(Post.class),
                    input,
                    new NullPointerException(
                            "Result of conversionService.convert(UpsertPostInput.class, Post.class) is null"));
        }
        return postRepository.save(post);
    }

    @Override
    public void deletePost(String id, MatchaUser matchaUser) {
        postRepository.deleteById(id);
    }
}
