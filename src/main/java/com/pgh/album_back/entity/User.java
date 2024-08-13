package com.pgh.album_back.entity;

import com.pgh.album_back.constant.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "[user]")
@SoftDelete
public class User extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String nickname;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Long point = 0L;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<GenreVote> genreVotes = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
        review.setUser(this);
    }

    public void addGenreVote(GenreVote genreVote) {
        genreVotes.add(genreVote);
        genreVote.setUser(this);
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setUser(this);
    }

    public Long getReviewCount() { // review 중 title이 있는 것들
        return reviews.stream().filter(review -> !review.getTitle().isEmpty()).count();
    }

    public Long getRatingCount() {
        return (long) reviews.size();
    }
}
