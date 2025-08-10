package com.wilton.matcha.core.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.wilton.matcha.common.domain.MatchaUser;
import com.wilton.matcha.core.domain.Post;
import com.wilton.matcha.core.repository.PostRepository;
import com.wilton.matcha.core.service.dto.PostSaveRequest;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.ConversionService;

public class PostServiceImplTest {

    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private ConversionService conversionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository, conversionService);
    }

    @Test
    void getPost_shouldReturnPostFromRepository() {
        // Given
        String postId = "post-123";
        MatchaUser user = new MatchaUser("id", "email", "firstName", "lastName", Collections.emptyList());
        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        Optional<Post> result = postService.getPost(postId, user);

        // Then
        assertTrue(result.isPresent());
        assertEquals(postId, result.get().getId());
        verify(postRepository, times(1)).findById(postId);
    }

    @Test
    void savePost_shouldConvertAndSavePost() {
        // Given
        PostSaveRequest request = new PostSaveRequest("post-1", "John", "Doe", 30, 5.8, "Test post");
        MatchaUser user = new MatchaUser("id", "email", "firstName", "lastName", Collections.emptyList());
        Post post = new Post();
        post.setId("post-1");

        when(conversionService.convert(request, Post.class)).thenReturn(post);
        when(postRepository.save(post)).thenReturn(post);

        // When
        Post result = postService.savePost(request, user);

        // Then
        assertNotNull(result);
        assertEquals("post-1", result.getId());
        verify(conversionService, times(1)).convert(request, Post.class);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    void savePost_shouldThrowWhenConversionFails() {
        // Given
        PostSaveRequest request = new PostSaveRequest("post-2", "Jane", "Doe", 25, 5.6, "Broken post");
        MatchaUser user = new MatchaUser("id", "email", "firstName", "lastName", Collections.emptyList());

        when(conversionService.convert(request, Post.class)).thenReturn(null);

        // When / Then
        ConversionFailedException exception =
                assertThrows(ConversionFailedException.class, () -> postService.savePost(request, user));

        verify(conversionService, times(1)).convert(request, Post.class);
        verify(postRepository, never()).save(any());
    }

    @Test
    void deletePost_shouldDeleteById() {
        // Given
        String postId = "delete-me";
        MatchaUser user = new MatchaUser("id", "email", "firstName", "lastName", Collections.emptyList());

        // When
        postService.deletePost(postId, user);

        // Then
        verify(postRepository, times(1)).deleteById(postId);
    }
}
