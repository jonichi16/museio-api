package com.springzr.museio.libs.upload.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.libs.upload.model.UploadResult;
import com.springzr.museio.libs.upload.service.UploadService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * Local implementation of {@link UploadService}.
 *
 * <p>This will upload images to ./uploads directory in the root of the project
 * </p>
 *
 */
@Service
@RequiredArgsConstructor
@Profile("local")
public class LocalUploadServiceImpl implements UploadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalUploadServiceImpl.class);

    @Value("${storage.upload-dir}")
    String uploadDir;

    @Override
    public UploadResult upload(MultipartFile file, String folderName) {
        return this.upload(file, folderName, null);
    }

    @Override
    public UploadResult upload(MultipartFile file, String folderName, String publicId) {

        try {
            Path destination = Paths.get(uploadDir, file.getOriginalFilename());
            Files.createDirectories(destination.getParent());
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            LOGGER.info("Saving images to: {}", destination);

            return UploadResult.builder()
                    .publicId(UUID.randomUUID().toString())
                    .url(destination.toAbsolutePath().toString())
                    .build();
        } catch (IOException ex) {
            LOGGER.error("Error: {}", ex.toString());
            throw new MSException("Upload failed", HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
        }
    }
}
