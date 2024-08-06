package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AlbumDetailsDTO extends EntryDetailsDTO {
    private List<String> types = new ArrayList<>();
    private List<AlbumTrack> albumTracks = new ArrayList<>();

    @Getter
    @Setter
    public static class AlbumTrack {
        private String id;
        private String number;
        private int position;
        private String title;
        private Long length;
    }

    public void addAlbumTrack(AlbumTrack albumTrack) {
        albumTracks.add(albumTrack);
    }

    public void addType(String type) {
        types.add(type);
    }
}
