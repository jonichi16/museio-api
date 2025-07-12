package com.springzr.museio.services.collection.service.impl;

import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.libs.common.util.TokenUtil;
import com.springzr.museio.libs.common.util.TransactionalHandler;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.repository.CollectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionServiceImplTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private TransactionalHandler transactionalHandler;

    @InjectMocks
    private CollectionServiceImpl collectionService;

    // --- GET /collections logic ---

    @Test
    void getCollectionsByPortfolio_shouldReturnAllCollections_whenPortfolioIsBlank() {
        int page = 1, size = 10;
        PageRequest pageRequest = PageRequest.of(0, size);

        List<Collection> collections = List.of(new Collection(1L, "Title", "PORTFOLIO_VISUAL", "desc", 1L));
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

        List<Collection> collections = List.of(new Collection(1L, "Title", portfolio, "desc", 1L));
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

    // --- POST /collection logic ---

    @Test
    void createCollection_shouldSaveAndReturnCollection() {
        // given
        CollectionRequest request = new CollectionRequest();
        request.setTitle("Modern Art");
        request.setDescription("A vibrant modern art collection");
        request.setPortfolio("PORTFOLIO_VISUAL");

        Collection saved = new Collection();
        saved.setTitle("Modern Art");
        saved.setDescription("A vibrant modern art collection");
        saved.setPortfolio("PORTFOLIO_VISUAL");
        saved.setAccountId(123L);

        try (MockedStatic<TokenUtil> mockedTokenUtil = mockStatic(TokenUtil.class)) {
            mockedTokenUtil.when(TokenUtil::getAccountId).thenReturn(123L);

            when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                    .thenAnswer(invocation -> {
                        Supplier<?> supplier = invocation.getArgument(0);
                        return supplier.get();
                    });

            when(collectionRepository.save(any(Collection.class))).thenReturn(saved);

            // when
            Collection result = collectionService.createCollection(request);

            // then
            verify(collectionRepository, times(1)).save(any(Collection.class));
            assertThat(result.getTitle()).isEqualTo("Modern Art");
            assertThat(result.getAccountId()).isEqualTo(123L);
        }
    }

    @Test
    void createCollection_whenInvalidPortfolio_shouldThrowException() {
        // given
        CollectionRequest request = new CollectionRequest();
        request.setTitle("Invalid Collection");
        request.setDescription("This should fail");
        request.setPortfolio("PORTFOLIO_INVALID");

        // when + then
        assertThatThrownBy(() -> collectionService.createCollection(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid portfolio type");

        verify(collectionRepository, never()).save(any());
        verify(transactionalHandler, never()).runInTransactionSupplier(any());
    }
}
