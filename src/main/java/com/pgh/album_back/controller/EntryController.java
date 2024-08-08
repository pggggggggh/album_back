package com.pgh.album_back.controller;

import com.pgh.album_back.dto.AddReviewDTO;
import com.pgh.album_back.dto.AlbumDetailsDTO;
import com.pgh.album_back.dto.AlbumSummaryDTO;
import com.pgh.album_back.entity.Album;
import com.pgh.album_back.entity.Entry;
import com.pgh.album_back.entity.Review;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.*;
import com.pgh.album_back.service.AlbumService;
import com.pgh.album_back.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EntryController {
    private final AlbumService albumService;
    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;
    private final UserRepository userRepository;
    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;
    private final EntryRepository entryRepository;

    @GetMapping("/albums")
    public ResponseEntity<Page<AlbumSummaryDTO>> getAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Album> albumPage = albumService.getAlbums(pageable);
        Page<AlbumSummaryDTO> dtoPage = albumPage.map(Album::toAlbumSummaryDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @Transactional
    @GetMapping("/{type}/details")
    public ResponseEntity<?> getEntryDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String type,
            @RequestParam String id) {
        if (type.equalsIgnoreCase("albums")) {
            AlbumDetailsDTO albumDetailsDTO = albumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Album not found"))
                    .toAlbumDetailsDTO();
            if (userDetails != null) {
                User user = userRepository.findByUsername(userDetails.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));
                Entry entry = entryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entry not found"));
                Optional<Review> myReviewOptional = reviewRepository.findFirstByUserAndEntry(user, entry);
                if (myReviewOptional.isPresent()) {
                    Review myReview = myReviewOptional.get();
                    AlbumDetailsDTO.Review myReviewDto = new AlbumDetailsDTO.Review();
                    myReviewDto.setUsername(myReview.getUser().getUsername());
                    myReviewDto.setUserNickname(myReview.getUser().getNickname());
                    myReviewDto.setRating(myReview.getRating());
                    myReviewDto.setTitle(myReview.getTitle());
                    myReviewDto.setContent(myReview.getContent());
                    myReviewDto.setCreatedAt(myReview.getCreatedAt());
                    myReviewDto.setUpdatedAt(myReview.getUpdatedAt());

                    albumDetailsDTO.setMyReview(myReviewDto);
                }
            }
            return ResponseEntity.ok(albumDetailsDTO);
        } else if (type.equalsIgnoreCase("tracks")) {
            return trackRepository.findById(id)
                    .map(track -> ResponseEntity.ok(track.toTrackDetailsDTO()))
                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid type: " + type);
    }

    @PostMapping("/entries/{entryId}/reviews")
    public ResponseEntity<?> addReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId,
            @Valid @RequestBody AddReviewDTO addReviewDTO) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        reviewService.addReview(entryId, userDetails.getUsername(), addReviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/entries/{entryId}/reviews")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId,
            @Valid @RequestBody AddReviewDTO addReviewDTO) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        reviewService.updateReview(entryId, userDetails.getUsername(), addReviewDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/entries/{entryId}/reviews")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        reviewService.deleteReview(entryId, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/entries/{entryId}/reviews")
    public ResponseEntity<?> getReviews(
            @PathVariable String entryId) {
        Entry entry = entryRepository.findById(entryId).orElseThrow(() -> new EntityNotFoundException("Entry not found"));
        return ResponseEntity.ok(reviewRepository.findAllByEntryOrderByCreatedAtDesc(entry)
                .stream().map(Review::toReviewResponseDTO).toList());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("sdf");
    }
}
