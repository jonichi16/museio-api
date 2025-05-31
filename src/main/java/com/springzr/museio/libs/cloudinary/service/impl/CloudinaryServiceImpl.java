package com.springzr.museio.libs.cloudinary.service.impl;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.springzr.museio.libs.cloudinary.model.CloudinaryResult;
import com.springzr.museio.libs.cloudinary.service.CloudinaryService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService {

    @Value("${spring.application.name}")
    String appName;
    @Value("${spring.profiles.active}")
    String profile;

    private final Cloudinary cloudinary;

    @Override
    public CloudinaryResult upload(MultipartFile file, String folderName) throws IOException {
        return this.upload(file, folderName, null);
    }

    @Override
    public CloudinaryResult upload(MultipartFile file, String folderName, String publicId) throws IOException {

        Map<String, Object> uploadParams = buildUploadParams(folderName, publicId);

        @SuppressWarnings("unchecked")
        Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), uploadParams);

        return CloudinaryResult.builder()
                .url(result.get("secure_url").toString())
                .publicId(result.get("public_id").toString())
                .build();
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
