package com.wilton.matcha.core.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.wilton.matcha.core.controller.dto.PostApiDto;
import com.wilton.matcha.core.domain.Post;
import org.junit.jupiter.api.Test;

public class PostToPostApiDtoResponseFullTest {

    private static final PostToPostApiDtoResponseFull converter = new PostToPostApiDtoResponseFull();

    @Test
    public void convert_shouldMapFieldsCorrectly() {
        // Given
        Post post = new Post();
        post.setId("123");
        post.setFirstName("Alice");
        post.setLastName("Smith");
        post.setAge(28);
        post.setHeight(5.7);
        post.setDescription("Software Engineer");

        // When
        PostApiDto.Response.Full result = converter.convert(post);

        // Then
        assertNotNull(result);
        assertEquals("123", result.id());
        assertEquals("Alice", result.firstName());
        assertEquals("Smith", result.lastName());
        assertEquals(28, result.age());
        assertEquals(5.7, result.height());
        assertEquals("Software Engineer", result.description());
    }
}
