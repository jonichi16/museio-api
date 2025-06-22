package com.springzr.museio.services.collection.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.model.response.CollectionsSuccessResponse;
import com.springzr.museio.services.collection.service.CollectionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionControllerTest {

    @Mock
    private CollectionService collectionService;

    @InjectMocks
    private CollectionController collectionController;

    @Test
    void getCollectionsByPortfolio_shouldCallServiceOnceAndReturnSuccessResponse() {
        // given
        String portfolio = "PORTFOLIO_VISUAL";
        int page = 1;
        int size = 10;

        List<Collection> mockCollections = List.of(
                new Collection(1L, "Title 1", portfolio),
                new Collection(2L, "Title 2", portfolio)
        );

        CollectionResponse.Pagination pagination = new CollectionResponse.Pagination(
                size, page, 2L, 1
        );

        CollectionResponse mockResponse = new CollectionResponse(mockCollections, pagination);

        when(collectionService.getCollectionsByPortfolio(portfolio, page, size))
                .thenReturn(mockResponse);

        // when
        ResponseEntity<MSResponse<?>> responseEntity = collectionController.getCollectionsByPortfolio(portfolio, page, size);

        // then
        verify(collectionService, times(1)).getCollectionsByPortfolio(portfolio, page, size);
        assertThat(responseEntity.getStatusCode().value()).isEqualTo(200);

        MSResponse<?> response = responseEntity.getBody();
        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Collections retrieved successfully");

        CollectionResponse data = (CollectionResponse) Objects.requireNonNull(response.getData());
        assertThat(data.pagination().page()).isEqualTo(1);
        assertThat(data.pagination().totalElements()).isEqualTo(2);
    }

    @Test
    void shouldCreatePaginationFromPage() {
        // given
        List<Collection> items = List.of(
                new Collection(1L, "Title 1", "PORTFOLIO_VISUAL"),
                new Collection(2L, "Title 2", "PORTFOLIO_VISUAL")
        );
        Page<Collection> page = new PageImpl<>(items, PageRequest.of(0, 10), 20);

        // when
        CollectionResponse.Pagination pagination = new CollectionResponse.Pagination(page);

        // then
        assertThat(pagination.size()).isEqualTo(10);
        assertThat(pagination.page()).isEqualTo(1); // 1-based
        assertThat(pagination.totalElements()).isEqualTo(20);
        assertThat(pagination.totalPages()).isEqualTo(2);
    }

    @Test
    void shouldCreateCollectionResponseCorrectly() {
        // given
        List<Collection> collections = List.of(
                new Collection(1L, "Title 1", "PORTFOLIO_VISUAL")
        );
        CollectionResponse.Pagination pagination = new CollectionResponse.Pagination(10, 1, 1, 1);

        // when
        CollectionResponse response = new CollectionResponse(collections, pagination);

        // then
        assertThat(response.collections()).hasSize(1);
        assertThat(response.collections().getFirst().getTitle()).isEqualTo("Title 1");

        assertThat(response.pagination().size()).isEqualTo(10);
        assertThat(response.pagination().page()).isEqualTo(1);
    }

    @Test
    void shouldCreateResponseWithCorrectData() {
        // Arrange
        List<Collection> collections = List.of(
                Collection.builder().id(1L).title("Title 1").portfolio("PORTFOLIO_VISUAL").build(),
                Collection.builder().id(2L).title("Title 2").portfolio("PORTFOLIO_VISUAL").build()
        );

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Collection> page = new PageImpl<>(collections, pageRequest, 2);

        // Act
        CollectionsSuccessResponse response = new CollectionsSuccessResponse(collections, page);

        // Assert
        assertThat(response.getCode()).isEqualTo(200);
        assertThat(response.getMessage()).isEqualTo("Collections retrieved successfully");
        assertThat(response.getData()).isNotNull();
        assertThat(response.getData().getCollections()).hasSize(2);
        assertThat(response.getData().getPagination()).isNotNull();
        assertThat(response.getData().getPagination().getPage()).isEqualTo(1); // 1-based
        assertThat(response.getData().getPagination().getSize()).isEqualTo(10);
        assertThat(response.getData().getPagination().getTotalElements()).isEqualTo(2L);
        assertThat(response.getData().getPagination().getTotalPages()).isEqualTo(1);
    }

    @Test
    void getErrorAndErrorCode_shouldReturnNull() {
        // Arrange
        List<Collection> collections = List.of();
        Page<Collection> page = new PageImpl<>(collections, PageRequest.of(0, 10), 0);

        CollectionsSuccessResponse response = new CollectionsSuccessResponse(collections, page);

        // Act & Assert
        assertThat(response.getError()).isNull();
        assertThat(response.getErrorCode()).isNull();
    }
}