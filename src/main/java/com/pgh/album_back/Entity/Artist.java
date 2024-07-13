package com.pgh.album_back.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Artist {
    @Id
    @Column(name = "artist_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "group")
    private Set<ArtistRelationship> groups = new HashSet<>();

    @OneToMany(mappedBy = "member")
    private Set<ArtistRelationship> members = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "artist")
    private Set<AlbumArtist> albums = new HashSet<>();

    public ArtistRelationship addToGroup(Artist group) {
        ArtistRelationship artistRelationship = new ArtistRelationship(group, this);
        groups.add(artistRelationship);
        group.getMembers().add(artistRelationship);
        return artistRelationship;
    }

    public ArtistRelationship removeFromGroup(Artist group) {
        ArtistRelationship artistRelationship = new ArtistRelationship(group, this);
        groups.remove(artistRelationship);
        group.getMembers().remove(artistRelationship);
        artistRelationship.setGroup(null);
        artistRelationship.setMember(null);
        return artistRelationship;
    }

    public static Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        return artist;
    }
}
