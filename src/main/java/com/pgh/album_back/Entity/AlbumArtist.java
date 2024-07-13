package com.pgh.album_back.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AlbumArtist extends BaseEntity {
    @Id
    @Column(name = "album_artist_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name="artist_id")
    private Artist artist;
}
