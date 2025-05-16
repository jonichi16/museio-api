package com.springzr.museio.services.auth.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;


/**
 * Response for /api/auth/token endpoint.
 *
 * @param accessToken jwt token
 * @param bearerType type of bearer to be use for header
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record TokenResponse(
    String accessToken,
    String bearerType
) {
}
