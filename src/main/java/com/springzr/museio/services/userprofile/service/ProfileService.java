package com.springzr.museio.services.userprofile.service;

import com.springzr.museio.services.userprofile.model.response.UserProfileResponse;

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
    UserProfileResponse getProfileByUsername(String username);
}
