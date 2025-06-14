package com.springzr.museio.services.collection.service.impl;


import com.springzr.museio.libs.common.constant.ErrorCode;
import com.springzr.museio.libs.common.exception.MSException;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.repository.CollectionRepository;
import com.springzr.museio.services.collection.service.CollectionService;
import java.util.List;
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

    private final CollectionRepository collectionRepository;

    @Override
    public CollectionResponse getCollectionsByPortfolio(String portfolio, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Collection> collectionsPage;

        if (portfolio == null || portfolio.isBlank()) {
            collectionsPage = collectionRepository.findAll(pageable);
        } else {
            List<String> validPortfolios = List.of("PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");

            if (!validPortfolios.contains(portfolio.toUpperCase())) {
                throw new MSException("Invalid portfolio value", HttpStatus.BAD_REQUEST,
                        ErrorCode.BAD_REQUEST);
            }

            collectionsPage = collectionRepository.findByPortfolio(portfolio, pageable);
        }

        return new CollectionResponse(collectionsPage.getContent(),
                new CollectionResponse.Pagination(collectionsPage));
    }
}