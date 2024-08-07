package com.pgh.album_back.dto.MusicBrainz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MusicBrainzArtistDTO {
    private String id;
    private String name;
    private String disambiguation;
    private String type;
    private String gender;
    private List<Relation> relations;

    private String country;
    private Area area;
    @JsonProperty(value = "begin-area")
    private Area beginArea;
    @JsonProperty(value = "end-area")
    private Area endArea;
    @JsonProperty(value = "life-span")
    private LifeSpan lifeSpan;

    @JsonProperty(value = "release-groups")
    private List<ReleaseGroup> releaseGroups;

    @Getter
    @Setter
    public static class Area {
        private String name;
    }

    @Getter
    @Setter
    public static class ReleaseGroup {
        private String id;
    }

    @Getter
    @Setter
    public static class Relation {
        Artist artist;
        String type;
        String direction;

        @Getter
        @Setter
        public static class Artist {
            String id;
        }
    }

    @Getter
    @Setter
    public static class LifeSpan {
        LocalDate begin;
        Boolean ended;
        LocalDate end;
    }
}
