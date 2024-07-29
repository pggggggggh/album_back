package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AlbumSummaryDTO {
    private String id;

    private String title;

    private String disambiguation;

    private LocalDate date;

    private Double avgRating;

    private List<Artist> artists = new ArrayList<>();

    @Getter
    @Setter
    public static class Artist {
        private String id;
        private String name;

        public Artist(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public void addArtist(String id, String name) {
        Artist artist = new Artist(id, name);
        artists.add(artist);
    }
}
