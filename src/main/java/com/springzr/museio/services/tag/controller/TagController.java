package com.springzr.museio.services.tag.controller;

import com.springzr.museio.libs.common.dto.MSResponse;
import com.springzr.museio.libs.common.dto.SuccessResponse;
import com.springzr.museio.services.tag.model.response.TagCountGetResponse;
import com.springzr.museio.services.tag.service.TagService;
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
 * Controller responsible for handling user tag-related endpoints.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TagController.class);

    /**
     * Retrieves tags that match the given keyword with pagination.
     *
     * @param keyword The keyword to search for in tag names.
     * @param page    The page number (defaults to 1).
     * @param size    The page size (defaults to 10).
     * @return A ResponseEntity containing tags with count info.
     */
    @GetMapping("/tags")
    public ResponseEntity<MSResponse<?>> getTagsByKeyWord(
            @RequestParam(required = true) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        LOGGER.info("START : /tags");

        try {
            TagCountGetResponse tags = tagService.getTagsByKeyword(keyword, page, size);
            HttpStatus status = HttpStatus.OK;

            MSResponse<TagCountGetResponse> response = SuccessResponse
                    .<TagCountGetResponse>builder()
                    .code(status.value())
                    .message("Tags retrieved successfully")
                    .data(tags)
                    .build();

            LOGGER.info("END : /tags");
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            LOGGER.error("Exception in /tags", e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

            com.springzr.museio.libs.common.dto.ErrorResponse<String> errorResponse =
                    com.springzr.museio.libs.common.dto.ErrorResponse.<String>builder()
                            .code(status.value())
                            .message("Failed to retrieve tags")
                            .error(e.getMessage())
                            .build();

            return ResponseEntity.status(status).body(errorResponse);
        }
    }
}
