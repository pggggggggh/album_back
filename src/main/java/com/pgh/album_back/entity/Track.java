package com.pgh.album_back.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Track extends Entry {
    @OneToMany(mappedBy = "track")
    private List<AlbumTrack> albums;
}
