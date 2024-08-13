package com.pgh.album_back.entity;

import com.pgh.album_back.dto.ReviewResponseDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SoftDelete;

@Entity
@Getter
@Setter
@SoftDelete
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "entry_id")
    private Entry entry;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Double rating;

    private String title;

    private String content;

    public ReviewResponseDTO toReviewResponseDTO() {
        ReviewResponseDTO reviewResponseDTO = new ReviewResponseDTO();
        reviewResponseDTO.setUsername(user.getUsername());
        reviewResponseDTO.setNickname(user.getNickname());
        reviewResponseDTO.setTitle(title);
        reviewResponseDTO.setContent(content);
        reviewResponseDTO.setRating(rating);
        return reviewResponseDTO;
    }
}
