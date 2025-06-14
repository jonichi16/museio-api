package com.springzr.museio.services.collection.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.collection.model.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;


/**
 * A success response wrapper for retrieving collections,
 * including collection data and pagination metadata.
 *
 * <p>This class extends {@link MSResponse} and formats
 * the response to include the list of collections
 * and associated pagination information.</p>
 */
@JsonPropertyOrder({ "success", "code", "message", "data", "timestamp" })
public class CollectionsSuccessResponse extends MSResponse<CollectionsSuccessResponse.Data> {

    /**
     * Constructs a success response containing the given collections and pagination information.
     *
     * @param collections the list of collections retrieved
     * @param pageInfo    the page object containing pagination metadata
     */
    public CollectionsSuccessResponse(List<Collection> collections, Page<?> pageInfo) {
        super(true, 200, "Collections retrieved successfully");
        this.data = new Data(collections, pageInfo);
    }

    private final Data data;

    @Override
    public Data getData() {
        return data;
    }

    @Override
    public Data getError() {
        return null;
    }

    @Override
    public String getErrorCode() {
        return null;
    }

    /**
     * Wrapper class holding the collections list and pagination metadata.
     */
    @Getter
    public static class Data {
        private final List<Collection> collections;
        private final Pagination pagination;

        /**
         * Constructs a data container with the given collection list and page info.
         *
         * @param collections the list of collections
         * @param page        the pagination information from Spring Data
         */
        public Data(List<Collection> collections, Page<?> page) {
            this.collections = collections;
            this.pagination = new Pagination(page);
        }

        /**
         * Contains pagination details for the current collection result.
         */
        @Getter
        public static class Pagination {
            private final int size;
            private final int page;
            private final long totalElements;
            private final int totalPages;

            /**
             * Constructs pagination details from a {@link Page} object.
             *
             * @param page the page metadata
             */
            public Pagination(Page<?> page) {
                this.size = page.getSize();
                this.page = page.getNumber() + 1; // Adjusted to 1-based indexing
                this.totalElements = page.getTotalElements();
                this.totalPages = page.getTotalPages();
            }
        }
    }
}
