package com.springzr.museio.services.collection.controller;


import com.springzr.museio.services.collection.model.Collection;
import com.springzr.museio.services.collection.model.request.CollectionRequest;
import com.springzr.museio.services.collection.model.response.CollectionResponse;
import com.springzr.museio.services.collection.service.CollectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/collection")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @PostMapping
    public ResponseEntity<CollectionResponse> createCollection(
            @Valid @RequestBody CollectionRequest request) {
        try {
            Collection saved = collectionService.createCollection(request);

            CollectionResponse resp = new CollectionResponse(
                    true,
                    201,
                    "Collection created successfully",
                    Map.of("collectionId", saved.getId())
            );

            return ResponseEntity.status(201).body(resp);

        } catch (IllegalArgumentException ex) {
            CollectionResponse error = new CollectionResponse(
                    false,
                    400,
                    ex.getMessage(),
                    null
            );
            return ResponseEntity.badRequest().body(error);
        }
    }
}
