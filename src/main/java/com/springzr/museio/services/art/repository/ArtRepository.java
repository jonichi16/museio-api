package com.springzr.museio.services.art.repository;

import com.springzr.museio.services.art.model.Art;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing {@link Art} entities.
 */
@Repository
public interface ArtRepository extends JpaRepository<Art, Long> {

    /**
     * Retrieves a paginated list of arts. If a collection_id is provided, findByCollectionId;
     * otherwise, fetches all collections (findAll).
     *
     * @param pageable the pagination and sorting information
     * @return a page of arts, optionally filtered by collection_id
     */
    @NonNull
    Page<Art> findAll(@NonNull Pageable pageable);

    /**
     * Retrieves arts filtered by collection_id with pagination support.
     * If the collection_id is null or empty, all arts are returned(findAll).
     *
     * @param collectionId the art to filter by collection_id;
     *                  if null or empty, all arts are retrieved
     * @param pageable  the pagination and sorting information
     * @return a page of arts, filtered by collection if specified
     */
    @EntityGraph(attributePaths = {"tags"})
    Page<Art> findByCollectionId(int collectionId, Pageable pageable);
}
