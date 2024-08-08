package com.pgh.album_back.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AddReviewDTO {
    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Short rating;
}
