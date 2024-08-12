package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TrackDetailsDTO extends EntryDetailsDTO {
    private Long length;

    private List<Album> appearsAt = new ArrayList<>();

    @Getter
    @Setter
    public static class Album {
        private String albumId;
        private String albumTitle;
        private Long albumReviewCount;
        private Double albumAverageRating;
        private String number;
    }

    public void addAppearsAt(Album album) {
        appearsAt.add(album);
    }
}
