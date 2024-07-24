package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ArtistDTO {
    private String id;
    private String name;
    private String disambiguation;
    private String type;
    private String gender;

    private String country;
    private String area;
    private String beginArea;
    private String endArea;
    private LocalDate beginDate;
    private LocalDate endDate;

    private List<String> albumIds = new ArrayList<>();
    private List<Relation> relations = new ArrayList<>();

    @Getter
    @Setter
    public static class Relation {
        String artistId;
        String type;
        String direction;
    }

    public void addAlbumId(String id) {
        albumIds.add(id);
    }

    public void addRelation(Relation relation) {
        relations.add(relation);
    }
}
