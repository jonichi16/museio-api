package com.springzr.museio.services.collection.service;


import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;

public interface CollectionService {
    Collection createCollection(CollectionRequest request);
}