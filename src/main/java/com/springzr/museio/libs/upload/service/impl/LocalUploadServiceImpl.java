package com.springzr.museio.libs.upload.service.impl;

import com.springzr.museio.libs.upload.model.UploadResult;
import com.springzr.museio.libs.upload.service.UploadService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Profile("local")
public class LocalUploadServiceImpl implements UploadService {

    @Value("${storage.upload-dir}")
    String uploadDir;

    @Override
    public UploadResult upload(MultipartFile file, String folderName) throws IOException {
        return this.upload(file, folderName, null);
    }

    @Override
    public UploadResult upload(MultipartFile file, String folderName, String publicId) throws IOException {
        Path destination = Paths.get(uploadDir, file.getOriginalFilename());
        Files.createDirectories(destination.getParent());
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        return UploadResult.builder()
                .publicId(UUID.randomUUID().toString())
                .url(destination.toAbsolutePath().toString())
                .build();
    }
}
