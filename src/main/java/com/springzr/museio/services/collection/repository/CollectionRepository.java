package com.springzr.museio.services.collection.repository;

import com.springzr.museio.services.collection.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for managing {@link Collection} entities.
 */
@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    /**
     * Retrieves a paginated list of collections. If a portfolio is provided, findByPortfolio;
     * otherwise, fetches all collections (findAll).
     *
     * @param pageable the pagination and sorting information
     * @return a page of collections, optionally filtered by portfolio
     */
    @NonNull Page<Collection> findAll(@NonNull Pageable pageable);

    /**
     * Retrieves collections filtered by portfolio with pagination support.
     * If the portfolio is null or empty, all collections are returned(findAll).
     *
     * @param portfolio the portfolio to filter by (e.g., "PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");
     *                  if null or empty, all collections are retrieved
     * @param pageable  the pagination and sorting information
     * @return a page of collections, filtered by portfolio if specified
     */
    Page<Collection> findByPortfolio(String portfolio, Pageable pageable);


}

