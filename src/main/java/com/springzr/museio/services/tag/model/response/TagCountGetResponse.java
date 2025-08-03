package com.springzr.museio.services.tag.model.response;

import java.util.List;
import org.springframework.data.domain.Page;

/**
 * Response DTO representing the result of a searching tags by keyword.
 * Includes operation success status, HTTP response code,
 * message, optional data (e.g., array of tags),
 * and a timestamp indicating when the response was generated.
 */
public record TagCountGetResponse(List<TagCountResponse> tags, Pagination pagination) {
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
