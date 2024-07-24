package com.pgh.album_back.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Credit extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "credit_id")
    private Long id;

    @ElementCollection
    private List<String> types;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Entry entry;

    public Credit(Artist artist, Entry entry) {
        this.artist = artist;
        this.entry = entry;
    }
}
