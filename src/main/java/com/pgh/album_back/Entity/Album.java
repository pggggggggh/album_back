package com.pgh.album_back.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Album extends BaseEntity {
    @Id
    @Column(name = "album_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @OneToMany(mappedBy = "album")
    private Set<AlbumArtist> artists = new HashSet<>();
}
