package com.springzr.museio.services.userprofile.repository;

import com.springzr.museio.services.userprofile.model.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link UserProfile} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations.
 * Includes a custom method to find a profile by username.
 */
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    /**
     * Finds a {@link UserProfile} by its username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the profile if found, or empty if not
     */
    Optional<UserProfile> findByUsername(String username);
}
