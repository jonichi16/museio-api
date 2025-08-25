package com.springzr.museio.services.art.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.art.model.response.ArtGetResponse;
import com.springzr.museio.services.art.service.ArtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller responsible for handling user collection-related endpoints.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtController {
    private final ArtService artService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ArtController.class);

    /**
     * Fetch arts.
     *
     * @param collectionId the collection_id to filter
     * @param page the page number to retrieve (1-based index).
     * @param size the number of items per page.
     * @return {@link ResponseEntity} containing {@link MSResponse} with the jwt access token
     *      and bearer type
     */
    @GetMapping("/arts")
    public ResponseEntity<MSResponse<ArtGetResponse>> getArtByCollectionId(
            @RequestParam(required = false) Integer collectionId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        LOGGER.info("START : /arts");

        ArtGetResponse art = artService.getArtByCollectionId(collectionId, page, size);
        HttpStatus status = HttpStatus.OK;
        MSResponse<ArtGetResponse> response = SuccessResponse.<ArtGetResponse>builder()
                .code(status.value())
                .message("Arts retrieved successfully")
                .data(art)
                .build();

        LOGGER.info("END : /arts");
        return ResponseEntity.status(status).body(response);
    }
}
