package com.springzr.museio.services.collection.service.impl;

import com.springzr.museio.libs.common.util.TokenUtil;
import com.springzr.museio.libs.common.util.TransactionalHandler;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.repository.CollectionRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.function.Supplier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CollectionServiceImplTest {

    @Mock
    private CollectionRepository repository;

    @Mock
    private TransactionalHandler transactionalHandler;

    @InjectMocks
    private CollectionServiceImpl collectionServiceImpl;

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

        // mock static method (if TokenUtil is static)
        mockStatic(TokenUtil.class).when(TokenUtil::getAccountId).thenReturn(123L);

        when(transactionalHandler.runInTransactionSupplier(any(Supplier.class)))
                .thenAnswer(invocation -> {
                    Supplier<?> supplier = invocation.getArgument(0);
                    return supplier.get();
                });

        when(repository.save(any(Collection.class))).thenReturn(saved);

        // when
        Collection result = collectionServiceImpl.createCollection(request);

        // then
        verify(repository, times(1)).save(any(Collection.class));
        assertThat(result.getTitle()).isEqualTo("Modern Art");
        assertThat(result.getAccountId()).isEqualTo(123L);
    }

    @Test
    void createCollection_whenInvalidPortfolio_shouldThrowException() {
        // given
        CollectionRequest request = new CollectionRequest();
        request.setTitle("Invalid Collection");
        request.setDescription("This should fail");
        request.setPortfolio("PORTFOLIO_INVALID");

        // when + then
        assertThatThrownBy(() -> collectionServiceImpl.createCollection(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid portfolio type");

        verify(repository, never()).save(any());
        verify(transactionalHandler, never()).runInTransactionSupplier(any());
    }
}
