package com.springzr.museio.services.profile.repository;

import com.springzr.museio.services.profile.model.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for accessing {@link Profile} entities.
 * Extends {@link JpaRepository} to provide standard CRUD operations.
 * Includes a custom method to find a profile by username.
 */
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    /**
     * Finds a {@link Profile} by its username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the profile if found, or empty if not
     */
    Optional<Profile> findByUsername(String username);
}
