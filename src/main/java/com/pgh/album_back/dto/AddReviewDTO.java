package com.pgh.album_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddReviewDTO {
    private String title;

    private String content;

    @NotNull
    private Short rating;
}
