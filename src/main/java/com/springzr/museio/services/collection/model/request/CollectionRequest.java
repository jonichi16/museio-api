package com.springzr.museio.services.collection.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Request DTO for creating or updating a collection.
 * Contains the title, description, and associated portfolio type.
 */
@Data
public class CollectionRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Portfolio is required")
    private String portfolio;
}
