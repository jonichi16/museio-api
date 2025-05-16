package com.springzr.museio.services.auth.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record TokenResponse(
    String accessToken,
    String bearerType
) {
}
