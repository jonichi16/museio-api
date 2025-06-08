package com.springzr.museio.services.collection.controller;

import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.auth.model.response.TokenResponse;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.model.response.CollectionsSuccessResponse;
import com.springzr.museio.libs.common.dto.ErrorResponse;
import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.repository.CollectionRepository;
import com.springzr.museio.services.collection.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CollectionController.class);

    @GetMapping("/collections")
    public ResponseEntity<MSResponse<?>> getCollectionsByPortfolio(
            @RequestParam(required = false) String portfolio,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        LOGGER.info("START : /collections");

        CollectionResponse collection = collectionService.getCollectionsByPortfolio(portfolio, page, size);

        HttpStatus status = HttpStatus.OK;
        MSResponse<CollectionResponse> response = SuccessResponse.<CollectionResponse>builder()
                .code(status.value())
                .message("Collections retrieved successfully")
                .data(collection)
                .build();

        LOGGER.info("END : /collections");
        return ResponseEntity.status(status).body(response);
    }

}
