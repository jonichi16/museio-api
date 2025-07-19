package com.springzr.museio.services.userprofile.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

/**
 * Represents a response containing user profile details.
 */
@Builder
@JsonPropertyOrder({ "username", "name", "email", "bio", "profilePicture" })
public record UserProfileResponse(
        String username,
        String name,
        String email,
        String bio,
        String profilePicture
) {}