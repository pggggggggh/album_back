package com.pgh.album_back.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Entry extends BaseEntity {
    @Id
    @Column(name = "entry_id")
    private String id;

    @Column(nullable = false)
    private String title;

    private String type;

    @Column(nullable = false)
    @OneToMany(mappedBy = "entry")
    private Set<EntryArtist> artists = new HashSet<>();

    @OneToMany(mappedBy = "entry")
    private List<Review> reviews = new ArrayList<>();
}
