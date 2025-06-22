package com.springzr.museio.services.collection.service.impl;

import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.repository.CollectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionServiceImplTest {

    @Mock
    private CollectionRepository collectionRepository;

    @InjectMocks
    private CollectionServiceImpl collectionService;

    @Test
    void getCollectionsByPortfolio_shouldReturnAllCollections_whenPortfolioIsBlank() {
        int page = 1, size = 10;
        PageRequest pageRequest = PageRequest.of(0, size);

        List<Collection> collections = List.of(new Collection(1L, "Title", "PORTFOLIO_VISUAL"));
        Page<Collection> pageResult = new PageImpl<>(collections, pageRequest, collections.size());

        when(collectionRepository.findAll(pageRequest)).thenReturn(pageResult);

        CollectionResponse response = collectionService.getCollectionsByPortfolio("", page, size);

        assertThat(response.collections()).hasSize(1);
        verify(collectionRepository).findAll(pageRequest);
    }

    @Test
    void getCollectionsByPortfolio_shouldCallFindByPortfolio_whenValidPortfolioIsGiven() {
        int page = 1, size = 10;
        String portfolio = "PORTFOLIO_VISUAL";
        PageRequest pageRequest = PageRequest.of(0, size);

        List<Collection> collections = List.of(new Collection(1L, "Title", portfolio));
        Page<Collection> pageResult = new PageImpl<>(collections, pageRequest, collections.size());

        when(collectionRepository.findByPortfolio(portfolio, pageRequest)).thenReturn(pageResult);

        CollectionResponse response = collectionService.getCollectionsByPortfolio(portfolio, page, size);

        assertThat(response.collections()).hasSize(1);
        verify(collectionRepository).findByPortfolio(portfolio, pageRequest);
    }

    @Test
    void getCollectionsByPortfolio_shouldThrowMSException_whenPortfolioIsInvalid() {
        int page = 1, size = 10;
        String invalidPortfolio = "INVALID";

        assertThrows(MSException.class, () -> {
            collectionService.getCollectionsByPortfolio(invalidPortfolio, page, size);
        });
    }
}