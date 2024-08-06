package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Artist extends BaseEntity {
    @Id
    @Column(name = "artist_id")
    private String id;

    @Column(nullable = false)
    private String name;
    private String disambiguation;

    private String type;
    private String gender;

    private String country;
    private String area;
    private String beginArea;
    private String endArea;
    private LocalDate beginDate;
    private LocalDate endDate;

    @OneToMany(mappedBy = "member")
    private Set<ArtistRelationship> groups = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<ArtistRelationship> members = new HashSet<>();

    @OneToMany(mappedBy = "artist")
    private Set<AlbumArtist> albums = new HashSet<>();

    @OneToMany(mappedBy = "artist")
    private Set<TrackArtist> tracks = new HashSet<>();

    @OneToMany(mappedBy = "artist")
    private Set<Credit> credits = new HashSet<>();

    public void addGroup(ArtistRelationship artistRelationship) {
        groups.add(artistRelationship);
    }

    public void addMember(ArtistRelationship artistRelationship) {
        members.add(artistRelationship);
    }

    public void addCredit(Credit credit) {
        credits.add(credit);
    }

    public void removeCredit(Credit credit) {
        credits.remove(credit);
    }

    public void addAlbum(AlbumArtist albumArtist) {
        albums.add(albumArtist);
    }

    public void addTrack(TrackArtist trackArtist) {
        tracks.add(trackArtist);
    }

    public void removeTrack(TrackArtist trackArtist) {
        tracks.remove(trackArtist);
    }

    public void removeGroup(ArtistRelationship artistRelationship) {
        groups.remove(artistRelationship);
    }

    public void removeMember(ArtistRelationship artistRelationship) {
        members.remove(artistRelationship);
    }

    public Artist(String id) {
        this.id = id;
    }

    public static Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setId(String.valueOf(UUID.randomUUID()));
        artist.setName(name);
        return artist;
    }
}
