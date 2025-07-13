package com.springzr.museio.services.art.service;

import com.springzr.museio.services.art.model.response.ArtGetResponse;

/**
 * Service interface for art-related operations.
 */
public interface ArtService {
    /**
     * Retrieves a paginated list of arts, optionally filtered by collection_id.
     *
     * @param collectionId the collection_id to filter
     * @param page the page number to retrieve (1-based index).
     * @param size the number of items per page.
     * @return {@link ArtGetResponse} containing the arts and pagination details.
     */
    ArtGetResponse getArtByCollectionId(Integer collectionId, int page, int size);
}
