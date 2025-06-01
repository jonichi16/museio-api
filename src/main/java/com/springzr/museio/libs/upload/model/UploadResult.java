package com.springzr.museio.libs.upload.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.springzr.museio.libs.upload.service.UploadService;
import lombok.Builder;

/**
 * Result of the upload by the {@link UploadService}.
 *
 * @param url location of the image saved
 * @param publicId unique identifier of the image
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record UploadResult(
        String url,
        String publicId
) {
}
