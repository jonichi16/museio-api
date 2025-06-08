package com.springzr.museio.services.collection.model.response;

import com.springzr.museio.services.collection.model.Collection;
import org.springframework.data.domain.Page;

import java.util.List;

public record CollectionResponse(List<Collection> collections, Pagination pagination) {

    public record Pagination(
            int size,
            int page,
            long totalElements,
            int totalPages
    ) {
        public Pagination(Page<?> page) {
            this(
                    page.getSize(),
                    page.getNumber() + 1, // 1-based indexing
                    page.getTotalElements(),
                    page.getTotalPages()
            );
        }
    }

}
