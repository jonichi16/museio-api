package com.springzr.museio.libs.upload.service;

import com.springzr.museio.libs.upload.model.UploadResult;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UploadService {

    UploadResult upload(MultipartFile file, String folderName) throws IOException;

    UploadResult upload(MultipartFile file, String folderName, String publicId) throws IOException;

}
