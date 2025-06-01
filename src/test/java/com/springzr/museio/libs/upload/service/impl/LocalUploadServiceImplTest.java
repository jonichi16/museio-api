package com.springzr.museio.libs.upload.service.impl;


import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.libs.upload.model.UploadResult;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LocalUploadServiceImplTest {

    @InjectMocks
    private LocalUploadServiceImpl localUploadServiceImpl;
    private Path tempDir;

    @BeforeEach
    void setUp() {
        tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "test-uploads-" + UUID.randomUUID());

        localUploadServiceImpl.uploadDir = tempDir.toString();
    }

    @AfterEach
    void tearDown() throws IOException {
        FileUtils.deleteDirectory(tempDir.toFile());
    }

    @Test
    void upload_shouldSaveImageLocally() {
        // given
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                "avatar.png",
                "image/png",
                "test image content".getBytes()
        );

        // when
        UploadResult result = localUploadServiceImpl.upload(mockFile, "/test/");
        Path storedPath = Paths.get(result.url());

        // then
        assertThat(Files.exists(tempDir)).isTrue();
        assertThat(Files.exists(storedPath)).isTrue();
    }

    @Test
    void upload_whenIOExceptionOccurs_shouldThrowMSException() throws IOException {
        // given
        MultipartFile mockFile = mock(MultipartFile.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(mockFile.getOriginalFilename()).thenReturn("test.png");
        when(mockFile.getInputStream()).thenReturn(mockInputStream);

        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            // when
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class)))
                    .thenThrow(new IOException("Simulated failure"));

            // then
            assertThatThrownBy(() -> localUploadServiceImpl.upload(mockFile, "test-folder", null))
                    .isInstanceOf(MSException.class)
                    .hasMessage("Upload failed")
                    .satisfies(ex -> {
                        MSException msEx = (MSException) ex;
                        assertThat(msEx.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
                        assertThat(msEx.getErrorCode()).isEqualTo(ErrorCode.BAD_REQUEST);
                    });
        }
    }
}
