package com.pgh.album_back.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserInfoResponseDTO {
    private String username;
    private String nickname;
    private String role;
    private Long point;
    private List<Notification> notifications = new ArrayList<>();

    @Getter
    @Setter
    public static class Notification {
        private String title;
        private String content;
        private Long sent;
        private LocalDateTime createdAt;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }
}
