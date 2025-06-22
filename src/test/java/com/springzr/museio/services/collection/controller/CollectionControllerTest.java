package com.springzr.museio.services.collection.controller;

import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.service.CollectionService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionControllerTest {

    @Mock
    private CollectionService collectionService;

    @InjectMocks
    private CollectionController collectionController;

    @Test
    void createCollection_shouldCallServiceOnce() {
        // given
        CollectionRequest request = new CollectionRequest();
        request.setTitle("Art Collection");
        request.setDescription("Modern art collection");
        request.setPortfolio("PORTFOLIO_VISUAL");

        Collection collection = new Collection();
        collection.setId(1L);
        collection.setAccountId(1L);
        collection.setTitle("Art Collection");
        collection.setDescription("Modern art collection");
        collection.setPortfolio("PORTFOLIO_VISUAL");

        when(collectionService.createCollection(request)).thenReturn(collection);

        // when
        collectionController.createCollection(request);

        // then
        verify(collectionService, times(1)).createCollection(request);
    }

    @Test
    void createCollection_shouldReturnCorrectResponse() {
        // given
        Long id = 1L;
        CollectionRequest request = new CollectionRequest();
        request.setTitle("Nature Photography");
        request.setDescription("Collection of nature photos");
        request.setPortfolio("portfolio-456");

        Collection saved = new Collection();
        saved.setId(1L);
        saved.setAccountId(id);
        saved.setTitle("Nature Photography");
        saved.setDescription("Collection of nature photos");
        saved.setPortfolio("portfolio-456");

        when(collectionService.createCollection(request)).thenReturn(saved);

        // when
        ResponseEntity<CollectionResponse> response = collectionController.createCollection(request);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(201);
        assertThat(response.getBody().getMessage()).isEqualTo("Collection created successfully");
        assertThat(response.getBody().getData().get("collectionId")).isEqualTo(id);
    }


    @Test
    void createCollection_shouldReturnBadRequestOnIllegalArgument() {
        // given
        CollectionRequest request = new CollectionRequest();
        request.setTitle(null); // invalid title
        request.setDescription("Bad data");
        request.setPortfolio("portfolio-999");

        when(collectionService.createCollection(request))
                .thenThrow(new IllegalArgumentException("Title is required"));

        // when
        ResponseEntity<CollectionResponse> response = collectionController.createCollection(request);

        // then
        assertThat(response.getStatusCode().value()).isEqualTo(400);
        assertThat(Objects.requireNonNull(response.getBody()).isSuccess()).isFalse();
        assertThat(response.getBody().getCode()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Title is required");
        assertThat(response.getBody().getData()).isNull();
    }
}
