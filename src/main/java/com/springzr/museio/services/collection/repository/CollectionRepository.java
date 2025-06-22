package com.springzr.museio.services.collection.repository;


import com.springzr.museio.services.collection.model.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionRepository extends JpaRepository<Collection, Long> {
}