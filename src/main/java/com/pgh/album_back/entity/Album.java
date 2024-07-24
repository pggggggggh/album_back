package com.pgh.album_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Album extends Entry {
    @ElementCollection
    private List<String> types = new ArrayList<>();

    @OneToMany(mappedBy = "album")
    private Set<AlbumTrack> tracks = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "album")
    protected Set<AlbumArtist> artists = new HashSet<>();

    public Album(String id) {
        super(id);
    }

    public void addTrack(AlbumTrack albumTrack) {
        tracks.add(albumTrack);
    }

    public void addArtist(AlbumArtist albumArtist) {
        artists.add(albumArtist);
    }
}