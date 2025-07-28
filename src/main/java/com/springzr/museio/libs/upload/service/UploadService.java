package com.springzr.museio.libs.upload.service;

import com.springzr.museio.libs.upload.model.UploadResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for upload-related operations.
 *
 */
@Service
public interface UploadService {

    /**
     * Upload a file without the publicId.
     *
     * @param file file to be uploaded
     * @param folderName name of folder for asset_folder
     * @return {@link UploadResult} contains the url and publicId
     */
    UploadResult upload(MultipartFile file, String folderName);


    /**
     * Upload a file without the publicId.
     *
     * @param file file to be uploaded
     * @param folderName name of folder for asset_folder
     * @param publicId manual id for publicId
     * @return {@link UploadResult} contains the url and publicId
     */
    UploadResult upload(MultipartFile file, String folderName, String publicId);

}
