package com.springzr.museio.libs.cloudinary.service;

import com.springzr.museio.libs.cloudinary.model.CloudinaryResult;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface CloudinaryService {

    CloudinaryResult upload(MultipartFile file, String folderName) throws IOException;

    CloudinaryResult upload(MultipartFile file, String folderName, String publicId) throws IOException;

}
