package com.springzr.museio.services.tag.service;

import com.springzr.museio.services.tag.model.Tag;
import java.util.List;

/**
 * Service interface for tag-related operations.
 */
public interface TagService {
    /**
     * Saves a tag with the given name if it doesn't already exist.
     *
     * @param name the name of the tag
     * @return the existing or newly saved tag
     */
    Tag saveTag(String name);

    /**
     * Saves a list of tags from the given list of tag names.
     * Existing tags (matched by name) are reused, and only new ones are saved.
     *
     * @param names the list of tag names to save
     * @return a list of saved or existing Tag objects
     */
    List<Tag> saveTags(List<String> names);
}
