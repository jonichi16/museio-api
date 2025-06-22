package com.springzr.museio.services.collection.service;

import com.springzr.museio.services.collection.model.response.CollectionResponse;

/**
 * Service interface for collection-related operations.
 */
public interface CollectionService {
    /**
     * Retrieves a paginated list of collections, optionally filtered by portfolio type.
     *
     * @param portfolio the portfolio to filter by (e.g., "PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");
     *                  if null or empty, all collections are retrieved.
     * @param page the page number to retrieve (1-based index).
     * @param size the number of items per page.
     * @return {@link CollectionResponse} containing the collections and pagination details.
     */
    CollectionResponse getCollectionsByPortfolio(String portfolio, int page, int size);
}
