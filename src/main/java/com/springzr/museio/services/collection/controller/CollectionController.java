package com.springzr.museio.services.collection.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.model.response.CollectionPostResponse;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.service.CollectionService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user collection-related endpoints.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionController.class);

    /**
     * Fetch collections.
     *
     * @param portfolio the portfolio to filter by (e.g., "PORTFOLIO_VISUAL", "PORTFOLIO_LITERARY");
     *                  if null or empty, all collections are retrieved.
     * @param page the page number to retrieve (1-based index).
     * @param size the number of items per page.
     * @return {@link ResponseEntity} containing {@link MSResponse} with the jwt access token
     *      and bearer type
     */
    @GetMapping("/collections")
    public ResponseEntity<MSResponse<?>> getCollectionsByPortfolio(
            @RequestParam(required = false) String portfolio,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        LOGGER.info("START : /collections");

        CollectionResponse collection = collectionService.getCollectionsByPortfolio(portfolio,
                page, size);

        HttpStatus status = HttpStatus.OK;
        MSResponse<CollectionResponse> response = SuccessResponse.<CollectionResponse>builder()
                .code(status.value())
                .message("Collections retrieved successfully")
                .data(collection)
                .build();

        LOGGER.info("END : /collections");
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Create collection.
     */
    @PostMapping("/collection")
    public ResponseEntity<CollectionPostResponse> createCollection(
            @Valid @RequestBody CollectionRequest request) {
        try {
            Collection saved = collectionService.createCollection(request);
            CollectionPostResponse resp = new CollectionPostResponse(
                    true, 201, "Collection created successfully",
                    Map.of("collectionId", saved.getId())
            );
            return ResponseEntity.status(201).body(resp);
        } catch (IllegalArgumentException ex) {
            CollectionPostResponse error = new CollectionPostResponse(
                    false, 400, ex.getMessage(), null
            );
            return ResponseEntity.badRequest().body(error);
        }
    }

}
