package com.springzr.museio.services.collection.service.impl;


import com.springzr.museio.libs.common.util.TokenUtil;
import com.springzr.museio.libs.common.util.TransactionalHandler;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.repository.CollectionRepository;
import com.springzr.museio.services.collection.service.CollectionService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CollectionServiceImpl implements CollectionService {

    private static final Set<String> VALID_PORTFOLIOS =
            Set.of("PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");

    private final CollectionRepository repository;
    private final TransactionalHandler transactionalHandler;

    public CollectionServiceImpl(
            CollectionRepository repository,
            TransactionalHandler transactionalHandler
    ) {
        this.repository = repository;
        this.transactionalHandler  = transactionalHandler;
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
            return repository.save(c);
        });
    }
}
