package com.pgh.album_back.service;

import com.pgh.album_back.dto.AddReviewDTO;
import com.pgh.album_back.entity.Entry;
import com.pgh.album_back.entity.Review;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.EntryRepository;
import com.pgh.album_back.repository.ReviewRepository;
import com.pgh.album_back.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final EntryRepository entryRepository;

    @Transactional
    public void updateReview(String entryId, String username, AddReviewDTO addReviewDTO) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review = reviewRepository.findFirstByUserAndEntry(user, entry)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        review.setTitle(addReviewDTO.getTitle());
        review.setContent(addReviewDTO.getContent());
        review.setRating(addReviewDTO.getRating());
        reviewRepository.save(review);
    }

    @Transactional
    public void addReview(String entryId, String username, AddReviewDTO addReviewDTO) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Optional<Review> existingReview = reviewRepository.findFirstByUserAndEntry(user, entry);
        if (existingReview.isPresent()) {
            throw new DataIntegrityViolationException("Review already exists");
        }
        Review review = new Review();
        review.setTitle(addReviewDTO.getTitle());
        review.setContent(addReviewDTO.getContent());
        review.setRating(addReviewDTO.getRating());
        user.addReview(review);
        entry.addReview(review);
        reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(String entryId, String username) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));

        Review review = reviewRepository.findFirstByUserAndEntry(user, entry)
                .orElseThrow(()-> new EntityNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }
}
