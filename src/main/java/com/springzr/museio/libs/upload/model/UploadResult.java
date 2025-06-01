package com.springzr.museio.libs.upload.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record UploadResult(
        String url,
        String publicId
) {
}
