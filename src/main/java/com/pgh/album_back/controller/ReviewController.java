package com.pgh.album_back.controller;

import com.pgh.album_back.dto.RecentReviewDTO;
import com.pgh.album_back.entity.Review;
import com.pgh.album_back.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<?> getAllReviews(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Review> reviewPage = reviewRepository.findAllByOrderByCreatedAtDesc(pageable);
        Page<RecentReviewDTO> dtoPage = reviewPage.map((review) -> {
            RecentReviewDTO dtoReview = new RecentReviewDTO();
            dtoReview.setUsername(review.getUser().getUsername());
            dtoReview.setNickname(review.getUser().getNickname());
            dtoReview.setTitle(review.getTitle());
            dtoReview.setContent(review.getContent());
            dtoReview.setRating(review.getRating());
            dtoReview.setEntryId(review.getEntry().getId());
            dtoReview.setEntryTitle(review.getEntry().getTitle());
            dtoReview.setCreatedAt(review.getCreatedAt());

            return dtoReview;
        });
        return ResponseEntity.ok(dtoPage);
    }
}
