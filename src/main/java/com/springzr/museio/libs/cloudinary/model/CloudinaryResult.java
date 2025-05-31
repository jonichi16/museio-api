package com.springzr.museio.libs.cloudinary.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record CloudinaryResult(
        String url,
        String publicId
) {
}
