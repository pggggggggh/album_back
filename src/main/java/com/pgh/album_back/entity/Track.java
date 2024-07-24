package com.pgh.album_back.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Track extends Entry {
    @OneToMany(mappedBy = "track")
    private Set<AlbumTrack> albums = new HashSet<>();

    @Column(nullable = false)
    @OneToMany(mappedBy = "track")
    protected Set<TrackArtist> artists = new HashSet<>();

    public void addAlbum(AlbumTrack albumTrack) {
        albums.add(albumTrack);
    }

    public void addArtist(TrackArtist trackArtist) {
        artists.add(trackArtist);
    }

    public Track(String id) {
        super(id);
    }
}
