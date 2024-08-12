package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LeaderboardsDTO {
    List<User> users = new ArrayList<>();

    @Getter
    @Setter
    public static class User {
        private String username;
        private String nickname;
        private Long point;
        private Long ratingCount;
        private Long reviewCount;
    }

    public void addUser(User user) {
        users.add(user);
    }
}
