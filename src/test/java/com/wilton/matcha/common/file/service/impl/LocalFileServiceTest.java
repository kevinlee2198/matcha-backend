package com.wilton.matcha.common.file.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class LocalFileServiceTest {
    private LocalFileService localFileService;

    @Mock
    private ResourceLoader resourceLoader;

    @TempDir
    private Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        localFileService = new LocalFileService(resourceLoader);

        // Inject temp path into private field
        try {
            var basePathField = LocalFileService.class.getDeclaredField("basePath");
            basePathField.setAccessible(true);
            basePathField.set(localFileService, tempDir.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSaveAndReadFileSuccessfully() throws IOException {
        // Given
        String key = "test-file.txt";
        byte[] content = "Hello, Matcha!".getBytes();
        Resource resource = new InputStreamResource(new ByteArrayInputStream(content));

        // When
        localFileService.save(key, resource);

        // And: simulate reading
        Path expectedPath = tempDir.resolve(key).toAbsolutePath();
        Resource expectedResource = new InputStreamResource(Files.newInputStream(expectedPath));
        when(resourceLoader.getResource(expectedPath.toString())).thenReturn(expectedResource);

        Resource readResource = localFileService.read(key);

        // Then
        byte[] readBytes = readResource.getInputStream().readAllBytes();
        assertThat(readBytes).isEqualTo(content);
    }

    @Test
    void testReadThrowsIfFileNotExists() {
        String key = "non-existent.txt";
        Path expectedPath = tempDir.resolve(key).toAbsolutePath();

        Resource resource = mock(Resource.class);
        when(resource.exists()).thenReturn(false);
        when(resource.isReadable()).thenReturn(false);
        when(resourceLoader.getResource(expectedPath.toString())).thenReturn(resource);

        assertThrows(IOException.class, () -> localFileService.read(key));
    }

    @Test
    void testDeleteFileSuccessfully() throws IOException {
        // Given
        String key = "delete-me.txt";
        Path filePath = tempDir.resolve(key);
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, "delete this".getBytes());

        assertThat(Files.exists(filePath)).isTrue();

        // When
        localFileService.delete(key);

        // Then
        assertThat(Files.exists(filePath)).isFalse();
    }

    @Test
    void testDeleteNonExistentFileDoesNotThrow() throws IOException {
        String key = "does-not-exist.txt";
        localFileService.delete(key); // Should not throw
    }
}
