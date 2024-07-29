package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TrackCreateDTO {
    private String id;
    private String title;
    private String disambiguation;
    private Long length;
    private LocalDate firstReleaseDate;
    private List<Artist> artists = new ArrayList<>();
    private Map<String, Credit> credits = new HashMap<>();


    @Getter
    @Setter
    public static class Artist {
        private String id;
        private String name;
    }
    @Getter
    @Setter
    public static class Credit {
        private String artistName;

        private List<String> types = new ArrayList<>();

        public void addType(String type) {
            if (types.contains(type)) return;
            types.add(type);
        }
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void addCredit(String artistId, Credit credit) {
        if (credits.containsKey(artistId)) {
            Credit existingCredit = credits.get(artistId);
            for (var type : credit.getTypes()) {
                existingCredit.addType(type);
            }
        } else {
            credits.put(artistId, credit);
        }
    }
}
