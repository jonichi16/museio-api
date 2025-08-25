package com.springzr.museio.services.art.model;

import com.springzr.museio.services.tag.model.Tag;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Entity that stores basic art details including id,
 * collection_id, title, description, tags, and imageUrl.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "art")
public class Art {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Collection ID is required")
    @Column(name = "collection_id")
    private int collectionId;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "art_tag",
            joinColumns = @JoinColumn(name = "art_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @NotBlank(message = "Image URL is required")
    @Column(name = "image_url")
    private String imageUrl;
}
