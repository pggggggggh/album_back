package com.pgh.album_back.repository;

import com.pgh.album_back.entity.Notification;
import com.pgh.album_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findTop5ByUserOrderByCreatedAtDesc(User user);
    boolean existsByUserAndTitleAndContent(User user, String title, String content);
}
