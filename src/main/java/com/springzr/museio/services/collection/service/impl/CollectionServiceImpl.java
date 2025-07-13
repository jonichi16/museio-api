package com.springzr.museio.services.collection.service.impl;

import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.libs.common.util.TokenUtil;
import com.springzr.museio.libs.common.util.TransactionalHandler;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.repository.CollectionRepository;
import com.springzr.museio.services.collection.service.CollectionService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


/**
 * Service class that implements {@link CollectionService}.
 */
@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {

    private static final Set<String> VALID_PORTFOLIOS =
            Set.of("PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");

    private final CollectionRepository collectionRepository;
    private final TransactionalHandler transactionalHandler;

    @Override
    public CollectionResponse getCollectionsByPortfolio(String portfolio, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Collection> collectionsPage;

        if (portfolio == null || portfolio.isBlank()) {
            collectionsPage = collectionRepository.findAll(pageable);
        } else {
            if (!VALID_PORTFOLIOS.contains(portfolio.toUpperCase())) {
                throw new MSException("Invalid portfolio value", HttpStatus.BAD_REQUEST,
                        ErrorCode.BAD_REQUEST);
            }

            collectionsPage = collectionRepository.findByPortfolio(portfolio, pageable);
        }

        return new CollectionResponse(
                collectionsPage.getContent(),
                new CollectionResponse.Pagination(collectionsPage)
        );
    }

    @Override
    public Collection createCollection(CollectionRequest req) {
        if (!VALID_PORTFOLIOS.contains(req.getPortfolio())) {
            throw new IllegalArgumentException("Invalid portfolio type");
        }

        return transactionalHandler.runInTransactionSupplier(() -> {
            Collection c = new Collection();
            c.setTitle(req.getTitle());
            c.setDescription(req.getDescription());
            c.setPortfolio(req.getPortfolio());
            c.setAccountId(TokenUtil.getAccountId());
            return collectionRepository.save(c);
        });
    }
}
