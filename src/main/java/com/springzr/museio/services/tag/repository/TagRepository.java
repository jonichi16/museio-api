package com.springzr.museio.services.tag.repository;

import com.springzr.museio.services.tag.model.Tag;
import com.springzr.museio.services.tag.model.response.TagCountResponse;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /**
     * Retrieves a list of tags by the provided keyword with fuzzy matching.
     *
     * @param keyword the keyword to search the tag
     * @param pageable pagination information
     * @return a Page of TagCountResponse
     */
    @Query(
            value = """
            SELECT t.id AS id, t.name AS name, COUNT(at.id) AS count
            FROM tag t
            LEFT JOIN art_tag at ON t.id = at.tag_id
            WHERE t.name % :keyword
            GROUP BY t.id, t.name
            ORDER BY similarity(t.name, :keyword) DESC
            """,
            countQuery = """
            SELECT COUNT(*)
            FROM tag t
            WHERE t.name % :keyword
            """,
            nativeQuery = true
    )
    Page<TagCountResponse> searchTagsWithCountFuzzy(@Param("keyword") String keyword,
                                                    Pageable pageable);
}
