package com.springzr.museio.services.collection.repository;

import com.springzr.museio.services.collection.model.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    @NonNull Page<Collection> findAll(@NonNull Pageable pageable);
    Page<Collection> findByPortfolio(String portfolio, Pageable pageable);
}

