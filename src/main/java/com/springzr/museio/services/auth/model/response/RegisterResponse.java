package com.springzr.museio.services.auth.model.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

/**
 * Response for /api/auth/register endpoint.
 *
 * @param username the user's username
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record RegisterResponse(
        String username
) {
}
