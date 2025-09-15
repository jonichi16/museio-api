package com.springzr.museio.services.tag.model.response;

/**
 * Data Transfer Object (DTO) for representing Tag entity responses.
 * Contains basic tag details such as id, name, and count.
 */
public record TagCountResponse(
        Long id,
        String name,
        long count
) {
}
