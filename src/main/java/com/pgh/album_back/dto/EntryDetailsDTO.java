package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class EntryDetailsDTO {
    private String id;
    private String title;
    private String disambiguation;
    private LocalDate date;
    private Long reviewCount;
    private Double averageRating;
    private String thumbUrlLarge;
    private String thumbUrlMedium;
    private String thumbUrlSmall;
    private List<Artist> artists = new ArrayList<>();
    private List<Credit> credits = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();
    private Map<String, Integer> genres = new HashMap<>();
    private Review myReview;

    @Getter
    @Setter
    public static class Artist {
        private String id;
        private String name;
    }

    @Getter
    @Setter
    public static class Credit {
        private List<String> types = new ArrayList<String>();
        private String artistId;
        private String artistName;
    }

    @Getter
    @Setter
    public static class Review {
        private String username;
        private String userNickname;
        private Double rating;
        private List<String> genreVotes;
        private String trackId;
        private String trackNumber;
        private String trackTitle;
        private String title;
        private String content;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void addCredit(Credit credit) {
        credits.add(credit);
    }
}
