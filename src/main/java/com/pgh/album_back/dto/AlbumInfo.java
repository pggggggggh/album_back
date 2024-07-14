package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbumInfo {
    public String id;

    public String title;

    public String disambiguation;

    public List<Media> media;

    @Getter
    public static class Media {
        private List<Track> tracks;
    }

    @Getter
    public static class Track {
        private String number;

        private int position;

        private Recording recording;
    }

    @Getter
    public static class Recording {
        private String id;
    }
}
