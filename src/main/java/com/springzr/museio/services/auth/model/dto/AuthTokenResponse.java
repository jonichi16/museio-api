package com.springzr.museio.services.auth.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import lombok.Builder;

/**
 * A Data Transfer Object (DTO) for representing authentication tokens.
 *
 * <p>This DTO encapsulates the details of an authentication response, including the token string
 * and the account ID associated with the token. It is used to provide a consistent structure for
 * authentication-related data in API responses.</p>
 *
 * <p>Fields that are {@code null} will be excluded from the JSON serialization due to the
 * {@link JsonInclude} annotation.</p>
 *
 * @param accountId the unique identifier of the account
 * @param accessToken the authentication token string
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record AuthTokenResponse(
        String accountId,
        String accessToken
) {
}
