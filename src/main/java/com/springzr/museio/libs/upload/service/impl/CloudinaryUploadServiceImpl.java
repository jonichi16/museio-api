package com.springzr.museio.libs.upload.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.libs.upload.model.UploadResult;
import com.springzr.museio.libs.upload.service.UploadService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Cloudinary implementation of {@link UploadService}.
 *
 * <p>This will upload the images to {@link Cloudinary} which will return the secure_url and
 *      public_id
 * </p>
 */
@Service
@RequiredArgsConstructor
@Profile("!local")
public class CloudinaryUploadServiceImpl implements UploadService {

    @Value("${spring.application.name}")
    String appName;
    @Value("${spring.profiles.active}")
    String profile;

    private final Cloudinary cloudinary;

    @Override
    public UploadResult upload(MultipartFile file, String folderName) {
        return this.upload(file, folderName, null);
    }

    @Override
    public UploadResult upload(MultipartFile file, String folderName, String publicId) {

        try {
            Map<String, Object> uploadParams = buildUploadParams(folderName, publicId);

            @SuppressWarnings("unchecked")
            Map<String, Object> result = cloudinary.uploader()
                    .upload(file.getBytes(), uploadParams);

            return UploadResult.builder()
                    .url(result.get("secure_url").toString())
                    .publicId(result.get("public_id").toString())
                    .build();
        } catch (IOException ex) {
            throw new MSException("Upload failed", HttpStatus.BAD_REQUEST, ErrorCode.BAD_REQUEST);
        }
    }

    private Map<String, Object> buildUploadParams(String folderName, String publicId) {
        String assetFolder = "/" + appName + "/" + profile + folderName;

        @SuppressWarnings("unchecked")
        Map<String, Object> uploadParams = ObjectUtils.asMap(
                "overwrite", true,
                "asset_folder", assetFolder,
                "format", "webp",
                "use_asset_folder_as_public_id_prefix", true,
                "use_filename_as_display_name", true
        );

        if (publicId != null) {
            uploadParams.put("public_id", publicId);
        }

        return uploadParams;
    }

}
