package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AlbumDTO {
    private String id;
    private String title;
    private String disambiguation;
    private LocalDate date;
    private List<String> trackRecordingIDs;
}
