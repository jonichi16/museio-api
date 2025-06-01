package com.springzr.museio.libs.upload.service.impl;


import com.springzr.museio.libs.upload.model.UploadResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

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
    void upload_shouldSaveImageLocally() throws Exception {
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

}
