package com.pgh.album_back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Album extends Entry {
    @OneToMany(mappedBy = "album")
    private List<AlbumTrack> tracks;
}