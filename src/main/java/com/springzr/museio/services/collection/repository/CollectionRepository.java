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
     * @param pageable the pagination and sorting information
     * @return an optional containing the portfolio is null, it will fetch all items, otherwise findByPortfolio
     */
    @NonNull Page<Collection> findAll(@NonNull Pageable pageable);

    /**
     * @param portfolio the portfolio to filter by (e.g., "PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");
     *                 if null or empty, all collections are retrieved.
     * @param pageable the pagination and sorting information
     * @return an optional containing the portfolio is null, it will fetch all items, otherwise findByPortfolio
     */
    Page<Collection> findByPortfolio(String portfolio, Pageable pageable);
}

