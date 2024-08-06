package com.pgh.album_back.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CoverArchiveThumbDTO {
    private List<Image> images;

    @Getter
    @Setter
    public static class Image {
        private Thumbnail thumbnails;

        @Getter
        @Setter
        public static class Thumbnail {
            @JsonProperty(value = "250")
            private String url250;
            @JsonProperty(value = "500")
            private String url500;
            @JsonProperty(value = "1200")
            private String url1200;
        }
    }
}
