package com.springzr.museio.services.tag.model;

import com.springzr.museio.services.art.model.Art;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This entity stores basic tag details including id and name.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private int count;

    @ManyToMany(mappedBy = "tags")
    private List<Art> arts;

    /**
     * Constructs a new Tag with the given name.
     *
     * @param name the name of the tag
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Constructs a new Tag with the given name,
     * and the count of appearances of the tag in art_tag table.
     *
     * @param name the name of the tag
     * @param count the count of appearances of the tag in art_tag table
     */
    public Tag(String name, int count) {
        this.name = name;
        this.count = count;
    }
}
