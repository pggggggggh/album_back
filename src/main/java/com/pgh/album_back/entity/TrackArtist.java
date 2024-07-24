package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TrackArtist extends BaseEntity {
    @Id
    @Column(name = "track_artist_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne
    @JoinColumn(name="artist_id")
    private Artist artist;

    public TrackArtist(Track track, Artist artist) {
        this.track = track;
        this.artist = artist;
    }
}
