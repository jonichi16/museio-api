package com.springzr.museio.services.auth.repository;

import com.springzr.museio.services.auth.model.Profile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Profile} entities.
 *
 */
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    /**
     * Check if username already exists.
     *
     * @param username the username to search for
     * @return a boolean if username already exists or not
     */
    boolean existsByUsername(String username);

    /**
     * Finds a {@link Profile} by its username.
     *
     * @param username the username to search for
     * @return an {@link Optional} containing the profile if found, or empty if not
     */
    Optional<Profile> findByUsername(String username);
}
