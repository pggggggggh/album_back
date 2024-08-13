package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RecentReviewDTO {
    private String username;
    private String nickname;
    private String title;
    private String content;
    private Double rating;
    private String entryId;
    private String entryTitle;
    private LocalDateTime createdAt;
}
