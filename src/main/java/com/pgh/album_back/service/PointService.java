package com.pgh.album_back.service;

import com.pgh.album_back.entity.*;
import com.pgh.album_back.repository.NotificationRepository;
import com.pgh.album_back.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final ReviewRepository reviewRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public void checkAllReviewed(Track track, User user) {
        for (var albumTrack:track.getAlbums()) {
            Album album = albumTrack.getAlbum();
            int flag = 1;
            for (var track2 : album.getValidTracks()) {
                if (!reviewRepository.existsByUserAndEntry(user, track2)) {
                    flag = 0;
                    break;
                }

                Review review = reviewRepository.findFirstByUserAndEntry(user, track2)
                        .orElse(null);
                if (review == null || review.getTitle().isEmpty()) {
                    flag = 0;
                    break;
                }
            }
            if (flag == 0) return;

            int pointsToAdd = 3;
            String title = "축하합니다!";
            String content = album.getTitle() + "의 모든 트랙에 한줄평/리뷰를 작성하여 " + pointsToAdd +
                    "포인트를 지급받으셨습니다!";

            if (notificationRepository.existsByUserAndTitleAndContent(user,title,content)) return;

            Notification notification = new Notification();
            user.addNotification(notification);
            notification.setTitle(title);
            notification.setContent(content);

            notificationRepository.save(notification);
            user.setPoint(user.getPoint() + pointsToAdd);
        }
    }
}
