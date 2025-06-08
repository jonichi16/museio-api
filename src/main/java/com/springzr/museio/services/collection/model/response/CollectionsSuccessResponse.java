package com.springzr.museio.services.collection.model.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.collection.model.Collection;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@JsonPropertyOrder({ "success", "code", "message", "data", "timestamp" })
public class CollectionsSuccessResponse extends MSResponse<CollectionsSuccessResponse.Data> {

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

    @Getter
    public static class Data {
        private final List<Collection> collections;
        private final Pagination pagination;

        public Data(List<Collection> collections, Page<?> page) {
            this.collections = collections;
            this.pagination = new Pagination(page);
        }

        @Getter
        public static class Pagination {
            private final int size;
            private final int page;
            private final long totalElements;
            private final int totalPages;

            public Pagination(Page<?> page) {
                this.size = page.getSize();
                this.page = page.getNumber() + 1;
                this.totalElements = page.getTotalElements();
                this.totalPages = page.getTotalPages();
            }
        }
    }
}
