package com.wilton.matcha.core.controller.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.wilton.matcha.core.controller.dto.PostApiDto;
import com.wilton.matcha.core.service.dto.PostSaveRequest;
import org.junit.jupiter.api.Test;

public class PostApiDtoRequestSaveToPostSaveRequestTest {

    private static final PostApiDtoRequestSaveToPostSaveRequest converter =
            new PostApiDtoRequestSaveToPostSaveRequest();

    @Test
    public void convert_shouldMapFieldsCorrectly() {
        // Given
        PostApiDto.Request.Save source = new PostApiDto.Request.Save("John", "Doe", 30, 5.9, "Sample description");

        // When
        PostSaveRequest result = converter.convert(source);

        // Then
        assertNotNull(result);
        assertNull(result.getId()); // Because 'id' is hardcoded as null in conversion
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(30, result.getAge());
        assertEquals(5.9, result.getHeight());
        assertEquals("Sample description", result.getDescription());
    }
}
