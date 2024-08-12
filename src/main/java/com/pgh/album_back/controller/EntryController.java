package com.pgh.album_back.controller;

import com.pgh.album_back.dto.AddReviewDTO;
import com.pgh.album_back.dto.AlbumDetailsDTO;
import com.pgh.album_back.dto.AlbumSummaryDTO;
import com.pgh.album_back.dto.TrackDetailsDTO;
import com.pgh.album_back.entity.Album;
import com.pgh.album_back.entity.Entry;
import com.pgh.album_back.entity.Review;
import com.pgh.album_back.entity.User;
import com.pgh.album_back.repository.*;
import com.pgh.album_back.service.AlbumService;
import com.pgh.album_back.service.EntryService;
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

import java.util.List;
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
    private final EntryService entryService;

    @GetMapping("/albums")
    public ResponseEntity<Page<AlbumSummaryDTO>> getAlbums(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "") String sort) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Album> albumPage = albumService.getAlbums(pageable,keyword,sort);
        Page<AlbumSummaryDTO> dtoPage = albumPage.map(Album::toAlbumSummaryDTO);
        return ResponseEntity.ok(dtoPage);
    }

    @Transactional
    @GetMapping("/{type}/{id}/details")
    public ResponseEntity<?> getEntryDetails(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String type,
            @PathVariable String id) {
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
                    myReviewDto.setGenreVotes(entryService.getMyVotes(id, userDetails.getUsername()));

                    albumDetailsDTO.setMyReview(myReviewDto);
                }
            }
            return ResponseEntity.ok(albumDetailsDTO);
        } else if (type.equalsIgnoreCase("tracks")) {
            TrackDetailsDTO trackDetailsDTO = trackRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Album not found"))
                    .toTrackDetailsDTO();
            if (userDetails != null) {
                User user = userRepository.findByUsername(userDetails.getUsername())
                        .orElseThrow(() -> new EntityNotFoundException("User not found"));
                Entry entry = entryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Entry not found"));
                Optional<Review> myReviewOptional = reviewRepository.findFirstByUserAndEntry(user, entry);
                if (myReviewOptional.isPresent()) {
                    Review myReview = myReviewOptional.get();
                    TrackDetailsDTO.Review myReviewDto = new TrackDetailsDTO.Review();
                    myReviewDto.setUsername(myReview.getUser().getUsername());
                    myReviewDto.setUserNickname(myReview.getUser().getNickname());
                    myReviewDto.setRating(myReview.getRating());
                    myReviewDto.setTitle(myReview.getTitle());
                    myReviewDto.setContent(myReview.getContent());
                    myReviewDto.setCreatedAt(myReview.getCreatedAt());
                    myReviewDto.setUpdatedAt(myReview.getUpdatedAt());

                    trackDetailsDTO.setMyReview(myReviewDto);
                }
            }
            return ResponseEntity.ok(trackDetailsDTO);
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

    @GetMapping("/entries/{entryId}/genres/vote")
    public ResponseEntity<?> getMyVotes(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        return ResponseEntity.ok(entryService.getMyVotes(entryId, userDetails.getUsername()));
    }

    @PostMapping("/entries/{entryId}/genres/vote")
    public ResponseEntity<?> voteGenre(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String entryId,
            @RequestBody List<String> genreNames) {
        if (userDetails == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        entryService.addVote(entryId, userDetails.getUsername(), genreNames);
        return ResponseEntity.ok().build();
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
