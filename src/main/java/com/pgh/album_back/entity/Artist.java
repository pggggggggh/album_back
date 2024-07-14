package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Artist extends BaseEntity {
    @Id
    @Column(name = "artist_id")
    private String id;

    @Column(nullable = false)
    private String name;
    private String disambiguation;

    private String type;
    private String gender;

    private LocalDate beginDate;
    private String beginArea;
    private LocalDate endDate;
    private String endArea;

    @OneToMany(mappedBy = "member")
    private Set<ArtistRelationship> groups = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<ArtistRelationship> members = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "artist")
    private Set<EntryArtist> albums = new HashSet<>();

    public static Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setId(String.valueOf(UUID.randomUUID()));
        artist.setName(name);
        return artist;
    }
}
