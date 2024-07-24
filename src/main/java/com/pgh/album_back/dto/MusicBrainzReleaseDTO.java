package com.pgh.album_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MusicBrainzReleaseDTO {
    private String id;
    private String title;
    private String disambiguation;
    private LocalDate date;

    @JsonProperty(value = "artist-credit")
    private List<ArtistCredit> artistCredits;

    @JsonProperty(value = "media")
    private List<Media> medias;

    @Getter
    public static class Media {
        private List<Track> tracks;
    }

    @Getter
    public static class Track {
        private String number;
        private int position;

        @JsonProperty(value = "artist-credit")
        private List<ArtistCredit> artistCredits;

        private Recording recording;
    }

    @Getter
    public static class ArtistCredit {
        private Artist artist;

        @Getter
        public static class Artist {
            private String id;
            private String name;
        }
    }

    @Getter
    public static class Recording {
        private String id;
        private String title;
        private String disambiguation;
        private Long length;

        @JsonProperty(value = "first-release-date")
        private LocalDate firstReleaseDate;
        private List<Relation> relations;

        @Getter
        public static class Relation {
            @JsonProperty(value = "target-type")
            private String targetType;
            private Relation.Artist artist;
            private String type;
            private List<String> attributes;
            private Relation.Work work;

            @Getter
            public static class Artist {
                private String id;
                private String name;
            }

            @Getter
            public static class Work {
                private List<Relation> relations;
            }
        }

    }
}
