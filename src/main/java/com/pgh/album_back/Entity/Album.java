package com.pgh.album_back.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Album {
    @Id
    @Column(name = "album_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany(mappedBy = "artist")
    @Column(nullable = false)
    private List<Artist> artists = new ArrayList<>();
}
