package com.springzr.museio.services.tag.service.impl;

import com.springzr.museio.services.tag.model.Tag;
import com.springzr.museio.services.tag.repository.TagRepository;
import com.springzr.museio.services.tag.service.TagService;
import java.util.List;
import java.util.stream.Collectors;
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
}
