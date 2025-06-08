package com.springzr.museio.services.collection.service;

import com.springzr.museio.services.collection.model.response.CollectionResponse;

public interface CollectionService {
    CollectionResponse getCollectionsByPortfolio(String portfolio, int page, int size);
}
