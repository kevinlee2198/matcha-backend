package com.wilton.matcha.common.file.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.wilton.matcha.common.exception.ResourceNotFoundException;
import com.wilton.matcha.common.file.StorageType;
import com.wilton.matcha.common.file.domain.ResourceMetadata;
import com.wilton.matcha.common.file.repository.ResourceMetadataRepository;
import com.wilton.matcha.common.file.service.dto.ResourceUploadRequest;
import com.wilton.matcha.common.file.service.dto.ResourceUploadResult;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public class DelegatingFileServiceTest {
    private DelegatingFileService delegatingFileService;

    @Mock
    private LocalFileService localFileService;

    @Mock
    private RemoteFileService remoteFileService;

    @Mock
    private ResourceMetadataRepository resourceMetadataRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        delegatingFileService =
                new DelegatingFileService(localFileService, remoteFileService, resourceMetadataRepository);
    }

    @Test
    public void testRead_LocalStorage_Success() throws IOException {
        String key = "file.txt";
        ResourceMetadata metadata = new ResourceMetadata();
        metadata.setKey(key);
        metadata.setStorageType(StorageType.LOCAL);

        Resource expectedResource = new ByteArrayResource("test content".getBytes());

        when(resourceMetadataRepository.findByKey(key)).thenReturn(Optional.of(metadata));
        when(localFileService.read(key)).thenReturn(expectedResource);

        Resource resource = delegatingFileService.read(key);

        assertThat(resource).isEqualTo(expectedResource);
        verify(localFileService).read(key);
        verifyNoInteractions(remoteFileService);
    }

    @Test
    public void testRead_RemoteStorage_Success() throws IOException {
        String key = "remote-file.txt";
        ResourceMetadata metadata = new ResourceMetadata();
        metadata.setKey(key);
        metadata.setStorageType(StorageType.REMOTE);

        Resource expectedResource = new ByteArrayResource("remote content".getBytes());

        when(resourceMetadataRepository.findByKey(key)).thenReturn(Optional.of(metadata));
        when(remoteFileService.read(key)).thenReturn(expectedResource);

        Resource resource = delegatingFileService.read(key);

        assertThat(resource).isEqualTo(expectedResource);
        verify(remoteFileService).read(key);
        verifyNoInteractions(localFileService);
    }

    @Test
    public void testRead_FileNotFound_ThrowsException() {
        String key = "missing.txt";
        when(resourceMetadataRepository.findByKey(key)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> delegatingFileService.read(key)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void testSave_LocalStorage_Success() throws IOException {
        String key = "local-file.txt";
        Resource resource = new ByteArrayResource("local data".getBytes()) {
            @Override
            public String getFilename() {
                return "local-file.txt";
            }

            @Override
            public long contentLength() {
                return 11;
            }
        };
        ResourceUploadRequest request = new ResourceUploadRequest(key, resource, StorageType.LOCAL);

        ResourceMetadata savedMetadata = new ResourceMetadata();
        savedMetadata.setKey(key);
        savedMetadata.setFilename("local-file.txt");
        savedMetadata.setSize(11L);
        savedMetadata.setStorageType(StorageType.LOCAL);

        when(resourceMetadataRepository.save(any())).thenReturn(savedMetadata);

        ResourceUploadResult result = delegatingFileService.save(request);

        verify(localFileService).save(key, resource);
        assertThat(result.success()).isTrue();
        assertThat(result.resourceMetadata()).isEqualTo(savedMetadata);
        assertThat(result.error()).isNull();
    }

    @Test
    public void testSave_RemoteStorage_Failure() throws IOException {
        String key = "bad-file.txt";
        Resource resource = new ByteArrayResource("bad data".getBytes()) {
            @Override
            public String getFilename() {
                return "bad-file.txt";
            }

            @Override
            public long contentLength() {
                return 8;
            }
        };
        ResourceUploadRequest request = new ResourceUploadRequest(key, resource, StorageType.REMOTE);

        doThrow(new IOException("Failed to save")).when(remoteFileService).save(eq(key), any());

        ResourceUploadResult result = delegatingFileService.save(request);

        verify(remoteFileService).save(key, resource);
        assertThat(result.success()).isFalse();
        assertThat(result.resourceMetadata()).isNull();
        assertThat(result.error()).isInstanceOf(IOException.class);
    }

    @Test
    public void testSave_Batch_MixedResults() throws IOException {
        Resource good = new ByteArrayResource("good".getBytes()) {
            @Override
            public String getFilename() {
                return "good.txt";
            }

            @Override
            public long contentLength() {
                return 4;
            }
        };
        Resource bad = new ByteArrayResource("bad".getBytes()) {
            @Override
            public String getFilename() {
                return "bad.txt";
            }

            @Override
            public long contentLength() {
                return 3;
            }
        };

        ResourceUploadRequest goodReq = new ResourceUploadRequest("good.txt", good, StorageType.LOCAL);
        ResourceUploadRequest badReq = new ResourceUploadRequest("bad.txt", bad, StorageType.REMOTE);

        when(resourceMetadataRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(localFileService).save(eq("good.txt"), any());
        doThrow(new IOException("Oops")).when(remoteFileService).save(eq("bad.txt"), any());

        List<ResourceUploadResult> results = delegatingFileService.save(List.of(goodReq, badReq));

        assertThat(results).hasSize(2);

        ResourceUploadResult goodResult = results.getFirst();
        assertThat(goodResult.success()).isTrue();
        assertThat(goodResult.resourceMetadata()).isNotNull();

        ResourceUploadResult badResult = results.get(1);
        assertThat(badResult.success()).isFalse();
        assertThat(badResult.error()).isInstanceOf(IOException.class);
    }

    @Test
    public void testDelete_LocalStorage_Success() throws IOException {
        String key = "delete-me.txt";
        ResourceMetadata metadata = new ResourceMetadata();
        metadata.setKey(key);
        metadata.setId("1");
        metadata.setStorageType(StorageType.LOCAL);

        when(resourceMetadataRepository.findByKey(key)).thenReturn(Optional.of(metadata));

        delegatingFileService.delete(key);

        verify(localFileService).delete(key);
        verify(resourceMetadataRepository).deleteById("1");
    }

    @Test
    public void testDelete_FileNotFound_DoesNothing() throws IOException {
        String key = "missing.txt";
        when(resourceMetadataRepository.findByKey(key)).thenReturn(Optional.empty());

        delegatingFileService.delete(key);

        verifyNoInteractions(localFileService, remoteFileService);
        verify(resourceMetadataRepository, never()).deleteById(any());
    }
}
