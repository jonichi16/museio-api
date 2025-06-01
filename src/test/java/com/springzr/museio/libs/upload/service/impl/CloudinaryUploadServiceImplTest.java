package com.springzr.museio.libs.upload.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.springzr.museio.libs.upload.model.UploadResult;
import java.io.IOException;
import java.util.Map;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.argThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CloudinaryUploadServiceImplTest {

    @Mock
    private Cloudinary cloudinary;
    private Uploader uploader;

    @InjectMocks
    private CloudinaryUploadServiceImpl cloudinaryServiceImpl;

    @BeforeEach
    void setUp() {
        cloudinaryServiceImpl.appName = "museio";
        cloudinaryServiceImpl.profile = "test";

        uploader = mock(Uploader.class);

        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void upload_shouldInvokeCloudinary() throws IOException {
        // given
        String folderName = "/johndoe/arts";
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpg", new byte[1000]
        );

        Map<String, Object> mockResponse = Map.of(
                "secure_url", "https://res.cloudinary.com/sample/test.webp",
                "public_id", "abc123"
        );

        // when
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(mockResponse);
        UploadResult uploadResult = cloudinaryServiceImpl.upload(file,  folderName);

        // then
        verify(uploader, times(1)).upload(any(byte[].class), argThat(params ->
                "webp".equals(params.get("format")) &&
                        "/museio/test/johndoe/arts".equals(params.get("asset_folder")) &&
                        Boolean.TRUE.equals(params.get("overwrite")) &&
                        Boolean.TRUE.equals(params.get("use_asset_folder_as_public_id_prefix")) &&
                        Boolean.TRUE.equals(params.get("use_filename_as_display_name"))
        ));
        assertThat(uploadResult.url()).isEqualTo(mockResponse.get("secure_url").toString());
        assertThat(uploadResult.publicId()).isEqualTo(mockResponse.get("public_id").toString());
    }

    @Test
    void upload_shouldHaveTheCorrectParams() throws IOException {
        // given
        String folderName = "/janedoe/profile_picture";
        String publicId = "profile_picture";
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpg", new byte[1000]
        );

        Map<String, Object> mockResponse = Map.of(
                "secure_url", "https://res.cloudinary.com/sample/test.webp",
                "public_id", "abc123"
        );

        // when
        when(uploader.upload(any(byte[].class), anyMap())).thenReturn(mockResponse);
        UploadResult uploadResult = cloudinaryServiceImpl.upload(file, folderName, publicId);

        // then
        verify(uploader, times(1)).upload(any(byte[].class), argThat(params ->
                "webp".equals(params.get("format")) &&
                        "/museio/test/janedoe/profile_picture".equals(params.get("asset_folder")) &&
                        Boolean.TRUE.equals(params.get("overwrite")) &&
                        Boolean.TRUE.equals(params.get("use_asset_folder_as_public_id_prefix")) &&
                        Boolean.TRUE.equals(params.get("use_filename_as_display_name")) &&
                        publicId.equals(params.get("public_id"))
        ));
        assertThat(uploadResult.url()).isEqualTo(mockResponse.get("secure_url").toString());
        assertThat(uploadResult.publicId()).isEqualTo(mockResponse.get("public_id").toString());
    }
}
