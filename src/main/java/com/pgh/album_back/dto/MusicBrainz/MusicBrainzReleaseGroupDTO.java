package com.pgh.album_back.dto.MusicBrainz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MusicBrainzReleaseGroupDTO {
    private List<Release> releases;

    @JsonProperty(value = "primary-type")
    private String primaryType;

    @JsonProperty(value = "secondary-types")
    private List<String> secondaryTypes;

    @Getter
    public static class Release {
        private String id;
        @JsonProperty(value = "media")
        private List<Media> medias;
    }

    @Getter
    public static class Media {
        private String format;
    }
}
