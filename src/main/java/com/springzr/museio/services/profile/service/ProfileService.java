package com.springzr.museio.services.profile.service;

import com.springzr.museio.services.profile.model.response.ProfileResponse;

/**
 * Service interface for collection-related operations.
 */
public interface ProfileService {
    /**
     * Fetch a user's profile by username.
     *
     * @param username the username of the profile to retrieve
     * @return the profile response
     */
    ProfileResponse getProfileByUsername(String username);
}
