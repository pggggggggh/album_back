package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewResponseDTO {
    private String username;
    private String nickname;
    private String title;
    private String content;
    private Short rating;
}
