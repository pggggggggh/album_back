package com.pgh.album_back.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Artist extends BaseEntity {
    @Id
    @Column(name = "artist_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "member")
    private Set<ArtistRelationship> groups = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<ArtistRelationship> members = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "artist")
    private Set<AlbumArtist> albums = new HashSet<>();

    public static Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        return artist;
    }
}
