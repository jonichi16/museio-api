package com.springzr.museio.services.art.model.response;

import com.springzr.museio.services.art.model.Art;
import com.springzr.museio.services.tag.model.Tag;
import java.util.List;

/**
 * Data Transfer Object (DTO) for representing Art entity responses.
 * Contains basic art details such as id, title, description, tags, and image URL.
 */
public record ArtResponse(
        Long id,
        String title,
        String description,
        List<String> tags,
        String imageUrl
) {

    /**
     * Converts an {@link Art} entity to an {@link ArtResponse} DTO.
     *
     * @param art the Art entity to convert
     * @return an instance of ArtResponse containing the art details
     */
    public static ArtResponse fromEntity(Art art) {
        List<String> tagNames = art.getTags().stream()
                .map(Tag::getName)
                .toList();

        return new ArtResponse(
                art.getId(),
                art.getTitle(),
                art.getDescription(),
                tagNames,
                art.getImageUrl()
        );
    }
}