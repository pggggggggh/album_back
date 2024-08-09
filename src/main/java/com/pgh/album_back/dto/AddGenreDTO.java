package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddGenreDTO {
    private String name;
    private String description;
    private String parentGenre;
}
