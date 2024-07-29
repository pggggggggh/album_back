package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AlbumCreateDTO {
    private String id;
    private String title;
    private String disambiguation;
    private List<String> types = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();
    private LocalDate date;
    private List<AlbumTrack> albumTracks = new ArrayList<>();

    @Getter
    @Setter
    public static class AlbumTrack {
        private String number;
        private int position;
        private TrackCreateDTO track;
    }

    @Getter
    @Setter
    public static class Artist {
        private String id;
        private String name;
    }

    public void addTrack(AlbumTrack track) {
        this.albumTracks.add(track);
    }
    public void addType(String type) {
        this.types.add(type);
    }
    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }
    public List<TrackCreateDTO> getTracks() {
        return albumTracks.stream().map(AlbumTrack::getTrack).collect(Collectors.toList());
    }
}
