package com.springzr.museio.services.tag.service.impl;

import com.springzr.museio.services.tag.model.Tag;
import com.springzr.museio.services.tag.model.response.TagCountGetResponse;
import com.springzr.museio.services.tag.model.response.TagCountResponse;
import com.springzr.museio.services.tag.repository.TagRepository;
import com.springzr.museio.services.tag.service.TagService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * Implementation of the TagService interface.
 */
@Service
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    /**
     * Constructs the TagServiceImpl with the required repository.
     *
     * @param tagRepository the tag repository
     */
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    /**
     * Saves a tag by name.
     * If a tag with the given name already exists in the database,
     * it is returned as-is. Otherwise, a new tag is created and saved.
     *
     * @param name the name of the tag to save
     * @return the existing or newly created Tag entity
     */
    @Override
    public Tag saveTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> tagRepository.save(new Tag(name)));
    }

    /**
     * Saves multiple tags based on a list of tag names.
     * For each tag name, this method checks whether the tag already exists.
     * If it does, the existing tag is reused; otherwise, a new tag is created and saved.
     *
     * @param names the list of tag names to save
     * @return a list of Tag entities, either existing or newly created
     */
    @Override
    public List<Tag> saveTags(List<String> names) {
        return names.stream()
                .map(this::saveTag)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of tags based on the provided keyword.
     *
     * <p>This method performs keyword sanitization (trims whitespace and removes spaces within
     * the keyword), then queries the repository to find tags whose names contain the keyword
     * (supports partial matches). The result includes a list of tags along with their associated
     * count (number of related arts) and pagination metadata such as page size, current page
     * number, total elements, and total pages.</p>
     *
     * @param keyword the keyword to search tags by; supports slightly misspelled matches.
     * @param page    the page number to retrieve (1-based index).
     * @param size    the number of items per page.
     * @return a {@link TagCountGetResponse} containing list of matched tags and pagination info.
     */
    @Override
    public TagCountGetResponse getTagsByKeyword(String keyword, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);

        Page<TagCountResponse> tagPage =
                tagRepository.searchTagsWithCountFuzzy(keyword, pageable);

        TagCountGetResponse.Pagination pagination =
                new TagCountGetResponse.Pagination(tagPage);

        return new TagCountGetResponse(tagPage.getContent(), pagination);
    }
}
