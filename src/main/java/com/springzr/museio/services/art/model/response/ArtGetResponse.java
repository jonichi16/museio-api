package com.springzr.museio.services.art.model.response;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Represents the response for an art retrieval request, including the list of arts
 * and pagination metadata.
 *
 * @param arts the list of retrieved arts
 * @param pagination  pagination details such as page size, current page, and total counts
 */
public record ArtGetResponse(List<ArtResponse> arts, Pagination pagination) {
    /**
     * Encapsulates pagination information for a paginated response.
     *
     * @param size          the number of items per page
     * @param page          the current page number (1-based)
     * @param totalElements the total number of elements across all pages
     * @param totalPages    the total number of available pages
     */
    public record Pagination(
            int size,
            int page,
            long totalElements,
            int totalPages
    ) {
        /**
         * Constructs a {@code Pagination} instance from a {@link Page} object.
         *
         * @param page the Spring Data {@link Page} object containing pagination metadata
         */
        public Pagination(Page<?> page) {
            this(
                    page.getSize(),
                    page.getNumber() + 1,
                    page.getTotalElements(),
                    page.getTotalPages()
            );
        }
    }
}
