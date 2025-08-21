package com.springzr.museio.services.tag.repository;

import com.springzr.museio.services.tag.model.Tag;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Tag} entities.
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     * Finds a tag by its name.
     *
     * @param name the name of the tag
     * @return an Optional containing the tag if found, or empty otherwise
     */
    Optional<Tag> findByName(String name);
}
